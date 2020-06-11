
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


            # Notify detection here:
            #   self._detection_callback(<arguments>)

            # Limits the framerate
            time.sleep(1.0/self._fps)
    

    def get_latest_frame(self) -> numpy.ndarray:
        """
        Returns the latest detected and decoded image frame
        """
        with self._frame_lock:
            return self._latest_frame 





    



    
