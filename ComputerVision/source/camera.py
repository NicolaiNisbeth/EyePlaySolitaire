
import threading
import cv2
import time
import base64


current_frame = None

_thread: threading.Thread = None
_lock = threading.Lock()
_run = True


def get_current_frame():
    with _lock:
        if current_frame is None:
            return None
        print(type(current_frame))
        return base64.b64encode(current_frame)
        # return current_frame


def start_camera():
    global _thread
    _thread = threading.Thread(target=_camera_loop)
    _thread.start()


def _camera_loop():
    resolution = (1280, 720)
    width, height = resolution

    cap = cv2.VideoCapture(0)
    cap.set(3, width)
    cap.set(4, height)

    global current_frame

    #print("Starting camera loop...")
    global _run
    while _run:
        ret, frame_read = cap.read()

        # frame_rgb = cv2.cvtColor(frame_read, cv2.COLOR_BGR2RGB)

        # frame_recolored = cv2.resize(
        #     frame_read,
        #     resolution,
        #     interpolation=cv2.INTER_LINEAR
        # )
        
        with _lock:
            current_frame = frame_read

    cap.release()


def _read_input():
    global _run
    while _run:
        cmd = input(">")
        if cmd == "exit":
            _run = False

        if cmd == "img":
            frame = get_current_frame()
            #print("Frame length: ", None if frame is None else len(frame))
            #print(frame[20])
    


if __name__ == "__main__":
    start_camera()
    _read_input()
    
    #threading._start_new_thread(_read_input, ())
    #_camera_loop()
    # while True:
    #     time.sleep(1)
    #     frame = get_current_frame()
    #     print("Frame:", frame if frame is not None else "None ")
    # _thread.join()