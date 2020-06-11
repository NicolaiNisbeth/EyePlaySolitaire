
import threading
import time

import camera


class Detector:


    def __init__(self, frequency=0.1):
        self._camera = camera.Camera()
        self._frequency = frequency
        
        self._run = True
    
        self._thread = threading.Thread(target=self._detect_loop)
        self._thread.start()


    def _detect_loop(self):
        while self._run:
            frame = self._camera.get_current_frame()
            if frame is None: continue

            time.sleep(self._frequency)
                        

    







    



    
