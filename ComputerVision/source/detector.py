
import threading
import time

import camera


class Detector:


    def __init__(self, fps=15.0):
        self._camera = camera.Camera()
        self._fps = fps
        
        self._run = True
    
        self._thread = threading.Thread(target=self._detect_loop)
        self._thread.start()


    def _detect_loop(self):
        while self._run:
            frame = self._camera.get_current_frame()
            if frame is None:
                continue
            
            # TODO: Detection here

            # Limits the framerate
            time.sleep(1.0/self._fps)
    







    



    
