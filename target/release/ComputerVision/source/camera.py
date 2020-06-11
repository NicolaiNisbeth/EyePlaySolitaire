
import threading
import timeit
import cv2
import numpy
from typing import Callable


class Camera:
    """
    Represents a recording instance of a camera using OpenCV.

    It starts a new thread on which it makes the recordings,
    and the latest frame may be retrieved using the
    'get_current_frame()' method
    """

    def __init__(self, resolution:(int, int)=(1280, 720), camera_id: int = 0, startup_callback: Callable = None):
        self._resolution = resolution
        self._width, self._height = resolution

        self._camera_id = camera_id

        self._current_frame: numpy.ndarray = None
        self._startup_callback = startup_callback

        # Whether or not the camera is running
        self._run = True

        # To secure only one has access to current frame
        self._lock = threading.Lock()

        # Thread to make the recordings on
        self._thread = threading.Thread(target=self._capture_loop)
        self._thread.start()       


    def _capture_loop(self):
        start = timeit.default_timer()
        cap = cv2.VideoCapture(self._camera_id)
        cap.set(3, self._width)
        cap.set(4, self._height)
        startup_time = timeit.default_timer() - start

        if self._startup_callback is not None:
            self._startup_callback(startup_time, self._resolution)

        while self._run:
            ret, frame_read = cap.read()
            with self._lock:
                self._current_frame = frame_read

        cap.release()


    def get_current_frame(self) -> numpy.ndarray: 
        """
        Returns the camera's last captured frame as a numpy.ndarray
        """
        with self._lock:
            return self._current_frame
            #return base64.b64encode(self._current_frame)




# = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = 

cam = None

def _read_input():
    global cam
    while cam._run:
        cmd = input("\n>")
        if cmd == "exit":
            cam._run = False

        if cmd == "img":
            frame = cam.get_current_frame()
            print(type(frame))
            #print("Frame length: ", None if frame is None else len(frame))
            #print(frame[20])
    


if __name__ == "__main__":
    cam = Camera()
    _read_input()
    
    #threading._start_new_thread(_read_input, ())
    #_camera_loop()
    # while True:
    #     time.sleep(1)
    #     frame = get_current_frame()
    #     print("Frame:", frame if frame is not None else "None ")
    # _thread.join()