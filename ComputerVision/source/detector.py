
import threading
import time
import cv2
import numpy

import camera
    


class Detector:

    def __init__(self, startup_callback, detection_callback, detection_fps=15.0, resolution: (int, int)=(1280, 720), camera_id: int = 0):
        self._camera = camera.Camera(resolution, camera_id, startup_callback)
        self._fps = detection_fps
        
        self._run = True
        self._frame_lock = threading.Lock()
        self._detection_callback = detection_callback

        self.output_resolution = (416, 416)

        # The latest detected frame
        self._latest_frame: numpy.ndarray = None
    
        self._thread = threading.Thread(target=self._detect_loop)
        self._thread.start()



    def _detect_loop(self):
        while self._run:
            frame = self._camera.get_current_frame()
            if frame is None:
                continue
        
            # TODO: Perform Detection here
            

            #TODO: Set Frame with detection boxes here
            with self._frame_lock:
                self._latest_frame = frame

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



def _detections_to_dict(detections):
    """
    Converts the output list of detections from darknet.py
    to a list of dictionary elements, which can be formatted as a json
    objects
    """
    json_detections = []

    for detection in detections:
        json_detection = {}
        json_detection["card"] = _label_to_dict(detection[0])
        json_detection["confidence"] = detection[1]
        json_detection["x"] = detection[2][0]
        json_detection["y"] = detection[2][1]
        json_detection["width"] = detection[2][2]
        json_detection["width"] = detection[2][3]
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
    else:
        if label[0] is "K": card["value"] =  13
        elif label[0] is "Q": card["value"] = 12
        elif label[0] is "J": card["value"] = 11
        elif label[0] is "A": card["value"] = 1
        else: card["value"] = int(label[0]) 
    
    return card
    


    
