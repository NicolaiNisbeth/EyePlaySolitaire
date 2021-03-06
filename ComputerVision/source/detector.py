
# import sys, os
# sys.path.append(os.path.abspath(os.path.join('..', 'darknet')))

import threading
import time
import cv2
import numpy
import camera
import sys
import timeit
    
from ctypes import *
import math
import random
import os
from darknet import darknet


class Detector:

    def __init__(self, detection_callback, detection_fps=2.0, resolution: (int, int)=(1280, 720), camera_id: int = 0):
        self._camera = camera.Camera(resolution, camera_id)

        time_start = timeit.default_timer()
        print(f"Initializing Darknet")
        
        self._fps = detection_fps
        
        self._run = True
        self._frame_lock = threading.Lock()
        self._detection_callback = detection_callback

        self.output_resolution = (1280, 720)

        # The latest detected frame
        self._latest_frame: numpy.ndarray = None


        self.paused = True
        
        # Darknet variables
        self._darknet_resolution = (0, 0)
        self._darknet_metaMain = None
        self._darknet_netMain = None
        self._darknet_image = None

        self._initialize_darknet()

        self._thread = None

        print(f"Darknet initialized in {timeit.default_timer()-time_start:.2f} seconds") 



    # Initializes the darknet library by loading config files and weights
    def _initialize_darknet(self):

        # Setup paths
        inputDir = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "input")
        configPath  = os.path.join(inputDir, "yolov3.cfg")
        weightPath  = os.path.join(inputDir, "card.weights") # Don't change this - change the file name instead
        metaPath = os.path.join(inputDir, "obj.data")
        setupObjPaths(metaPath, os.path.join(inputDir, "obj.names"))

        if not os.path.exists(weightPath):
            print(f"Couldn't find weights path '{weightPath}'")
            sys.exit(101)

        self._darknet_netMain = darknet.load_net_custom(configPath.encode("ascii"), weightPath.encode("ascii"), 0, 1)  # batch size = 1
        self._darknet_metaMain = darknet.load_meta(metaPath.encode("ascii"))

        print("Finished loading")

        # Not sure what is going on here?
        try:
            with open(metaPath) as metaFH:
                metaContents = metaFH.read()
                import re
                match = re.search("names *= *(.*)$", metaContents,
                                  re.IGNORECASE | re.MULTILINE)
                if match:
                    result = match.group(1)
                else:
                    result = None
                try:
                    if os.path.exists(result):
                        with open(result) as namesFH:
                            namesList = namesFH.read().strip().split("\n")
                            altNames = [x.strip() for x in namesList]
                except TypeError as e:
                    print(e)
                    sys.exit(1337)
        except Exception as e:
            print(e)
            sys.exit(1337)

        self._darknet_resolution = (darknet.network_width(self._darknet_netMain), darknet.network_height(self._darknet_netMain))
        self._darknet_image = darknet.make_image(self._darknet_resolution[0], self._darknet_resolution[1], 3)




    def start_detections(self):
        self._thread = threading.Thread(target=self._detect_loop)
        self._thread.start()


    # Primary loop of the computer vision, which performs the actual detections
    def _detect_loop(self):
        while self._run:
            frame = self._camera.get_current_frame()

            if not self.paused:
                if frame is None:
                    continue
            
                detections = self._detect(frame)

                if self._detection_callback is not None:
                    self._detection_callback(_detections_to_dict(detections), self._darknet_resolution[0], self._darknet_resolution[1])

                scaledDetections = _scale_detections(detections, self._camera._resolution[0]/self._darknet_resolution[0], self._camera._resolution[1]/self._darknet_resolution[1])

                frame = cvDrawBoxes(scaledDetections, frame)
                frame = cvDrawSectionLines(frame, self._camera._resolution[0], self._camera._resolution[1])

            with self._frame_lock:
                self._latest_frame = frame

            # Limits the framerate
            time.sleep(1.0/self._fps)
    


    def _detect(self, image):
        # Adjust image to darknet compatible
        frame_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        frame_resized = cv2.resize(frame_rgb, self._darknet_resolution, interpolation=cv2.INTER_LINEAR)
        darknet.copy_image_from_bytes(self._darknet_image, frame_resized.tobytes())
        
        # Perform detections
        detections = darknet.detect_image(self._darknet_netMain, self._darknet_metaMain, self._darknet_image, thresh=0.25)
        
        return detections



    def get_latest_frame(self) -> numpy.ndarray:
        """
        Returns the latest detected and decoded image frame
        """
        with self._frame_lock:
            return self._latest_frame 




def _scale_detections(detections, widthScale, heightScale):
    scaledDetections = []
    for detection in detections:
        scaledDetection = (
            detection[0], detection[1],
            (
                detection[2][0] * widthScale,
                detection[2][1] * heightScale,
                detection[2][2] * widthScale,
                detection[2][3] * heightScale
            )
        )
        scaledDetections.append(scaledDetection)
    return scaledDetections


def _detections_to_dict(detections):
    """
    Converts the output list of detections from darknet.py
    to a list of dictionary elements, which can be formatted as a json
    objects
    """
    json_detections = []

    for detection in detections:
        json_detection = {}

        # Decode card label
        try:
            json_detection["card"] = _label_to_dict(detection[0].decode())
        except ValueError as err:
            print(err)
            continue            

        json_detection["confidence"] = truncate(detection[1], decimals=2)
        json_detection["x"] = truncate(detection[2][0], decimals=2)
        json_detection["y"] = truncate(detection[2][1], decimals=2)
        json_detection["width"] = truncate(detection[2][2], decimals=2)
        json_detection["height"] = truncate(detection[2][3], decimals=2)
        json_detections.append(json_detection)

    return json_detections


def _label_to_dict(label):
    """
    Extracts the value and suit of a card from a label in the format
    '10d' or 'XXy' where XX is the value and y is the suit.
    Constructs and returns a dictionary representing the card with
    value and suit
    """
    card = {}
    card["suit"] = label[-1].capitalize()
    
    if len(label) is 3:
        card["value"] = 10
    elif len(label) is 2:
        if label[0] is "K": card["value"] = 13
        elif label[0] is "Q": card["value"] = 12
        elif label[0] is "J": card["value"] = 11
        elif label[0] is "A": card["value"] = 1
        else: card["value"] = int(label[0])
    else:
        raise ValueError(f"Detection label length is not correct (was {len(label)})")
    
    return card


def convertBack(x, y, w, h):
    xmin = int(round(x - (w / 2)))
    xmax = int(round(x + (w / 2)))
    ymin = int(round(y - (h / 2)))
    ymax = int(round(y + (h / 2)))
    return xmin, ymin, xmax, ymax




def cvDrawBoxes(detections, img):
    for detection in detections:
        x, y, w, h = detection[2][0], \
                     detection[2][1], \
                     detection[2][2], \
                     detection[2][3]
        xmin, ymin, xmax, ymax = convertBack(
            float(x), float(y), float(w), float(h))
        pt1 = (xmin, ymin)
        pt2 = (xmax, ymax)
        cv2.rectangle(img, pt1, pt2, (0, 255, 0), 1)
        cv2.putText(img,
                    detection[0].decode() +
                    " [" + str(round(detection[1] * 100, 2)) + "]",
                    (pt1[0], pt2[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                    [0, 255, 0], 2)
    return img


def cvDrawSectionLines(img, width, height):

    offset_W = 0.15
    stock_H = 0.27
    tableau_W = 0.1

    #Borders
    x, y, w, h = 0,0, int(round(width*offset_W)), height
    cv2.rectangle(img, (x,y), (w,h), (0, 0, 0), -1)
    x, y, w, h = int(round(width-(width*offset_W))),0, width, height
    cv2.rectangle(img, (x,y), (w,h), (0, 0, 0), -1)

    #Horizontal Line
    x1 = int(round(width*offset_W))
    y1 = int(round(height*stock_H))
    x2 = int(round(width-(width*offset_W)))
    y2 = int(round(height*stock_H))
    cv2.line(img,(x1,y1),(x2,y2),(255,0,0),5)

    #Lines
    for x in range(8):
        x1 = int(round(width*offset_W))+int(round(width*tableau_W*x))
        y1 = 0
        x2 = int(round(width*offset_W))+int(round(width*tableau_W*x))
        y2 = height
        if(x == 1 or x == 2 or x == 2):
            y1 = int(round(height*stock_H))
        cv2.line(img,(x1,y1),(x2,y2),(255,0,0),5)

    return img


def truncate(number, decimals=0):
    """
    Returns a value truncated to a specific number of decimal places.
    Source: #https://kodify.net/python/math/truncate-decimals/
    """
    if not isinstance(decimals, int):
        raise TypeError("decimal places must be an integer.")
    elif decimals < 0:
        raise ValueError("decimal places has to be 0 or more.")
    elif decimals == 0:
        return math.trunc(number)

    factor = 10.0 ** decimals
    return math.trunc(number * factor) / factor



# Adjusts the path within the obj.data for the obj.names file, so that is has an
# absolute path on this local system
def setupObjPaths(objDataPath, objNamesPath):

    objDataFile = open(objDataPath,'r')
    lines = objDataFile.read().splitlines()
    objDataFile.close()
    objDataFile = open(objDataPath, 'w')

    for line in lines:
        tokens = line.split(" ")
        if tokens[0] == "names":
            objDataFile.write("names = " + objNamesPath + "\n")
        else:
            objDataFile.write(line + "\n")

    objDataFile.close()