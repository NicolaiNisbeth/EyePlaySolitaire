
import threading
import time
import cv2
import numpy
import camera

from ctypes import *
import math
import random
import os
import numpy as np
import darknet

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
                    (pt1[0], pt1[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                    [0, 255, 0], 2)
    return img

class Detector:

    def __init__(self, startup_callback, detection_callback, detection_fps=15.0, resolution: (int, int)=(1280, 720), camera_id: int = 0):
        self._camera = camera.Camera(resolution, camera_id, startup_callback)
        self._fps = detection_fps
        
        self._run = True
        self._frame_lock = threading.Lock()
        self._detection_callback = detection_callback

        # The latest detected frame
        self._latest_frame: numpy.ndarray = None
    
        self._thread = threading.Thread(target=self._detect_loop)
        self._thread.start()



    def _detect_loop(self):


        configPath = "C:/Users/willi/IdeaProjects/EyePlaySolitaire1/ComputerVision/source/files/yolov3_custom.cfg"
        weightPath = "C:/Users/willi/IdeaProjects/EyePlaySolitaire1/ComputerVision/source/files/yolov3_custom_last1.weights"
        metaPath = "C:/Users/willi/IdeaProjects/EyePlaySolitaire1/ComputerVision/source/files/obj.data"


        netMain = darknet.load_net_custom(configPath.encode(
                "ascii"), weightPath.encode("ascii"), 0, 1)  # batch size = 1

        metaMain = darknet.load_meta(metaPath.encode("ascii"))

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
                except TypeError:
                    pass
        except Exception:
            pass


        darknet_image = darknet.make_image(darknet.network_width(netMain), darknet.network_height(netMain), 3)


        while self._run:
            frame = self._camera.get_current_frame()
            if frame is None:
                continue
        
            # TODO: Perform Detection here

            frame_read = frame
            frame_rgb = cv2.cvtColor(frame_read, cv2.COLOR_BGR2RGB)
            frame_resized = cv2.resize(frame_rgb,
                                       (darknet.network_width(netMain),
                                        darknet.network_height(netMain)),
                                       interpolation=cv2.INTER_LINEAR)
            print("width", darknet.network_width(netMain))
            print("height", darknet.network_height(netMain))
            print("frame_resized", len(frame_resized[0]))
            darknet.copy_image_from_bytes(darknet_image,frame_resized.tobytes())

            detections = darknet.detect_image(netMain, metaMain, darknet_image, thresh=0.25)
            image = cvDrawBoxes(detections, frame_resized)
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            image = cv2.resize(image,
                                       1280,
                                        720,
                                       interpolation=cv2.INTER_LINEAR)
            print("image", len(image))

            #TODO: Set Frame with detection boxes here
            with self._frame_lock:
                self._latest_frame = image

            #TODO: Notify detection here:
            #   self._detection_callback(<arguments>)

            # Limits the framerate
            time.sleep(1.0/self._fps)
    

    def get_latest_frame(self) -> numpy.ndarray:
        """
        Returns the latest detected and decoded image frame
        """
        with self._frame_lock:
            return self._latest_frame 





    



    
