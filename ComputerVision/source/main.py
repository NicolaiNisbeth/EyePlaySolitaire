
import sys
import json
import time
import base64
import io
import cv2

from message import Message
from connector import Connector
from detector import Detector

connector = None
detector = None

def main():
    global connector
    global detector

    # Get port argument
    if len(sys.argv) < 2:
        raise Exception("Missing port argument")
    port = None
    try:
        port = int(sys.argv[1])
        if type(port) is not int or port < 1 or port > 65535:
            raise Exception()
    except:
        raise TypeError("Port argument must be an integer between 1 and 65535 (inclusive)")

    # Connect to server
    connector = Connector(port, message_received)
    
    # Start Detector
    detector = Detector(new_detections)
    detector.start_detections()

    # Notify server that the client has started
    connector.send_message(Message(100, "{}"))
    
    # Keep the program alive
    detector._thread.join()

    


# ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    
def message_received(msg: Message):
    global detector

    if msg._code is 200:
        detector.paused = False

    if msg._code is 201:
        detector.paused = True

    if msg._code is 202:
        send_image()


def new_detections(detections, width, height):
    # Notify server that new detections are ready
    data = {"detections" : detections, "width" : width, "height" : height}
    connector.send_message(Message(101, json.dumps(data)), False)


def send_image():
    global connector
    global detector
    image = detector.get_latest_frame()
    width, height = detector.output_resolution

    data = None
    if image is None:
        # If not image has been captured yet, we just return an empty json object
        # which will cause the server to request a new image
        data = {}
    else:
        # First encode into bxase64 bytes, and then into ascii string
        encoded_image = base64.b64encode(image).decode('ascii')
        data = {"image": encoded_image, "width": width, "height": height}
    connector.send_message(Message(102, json.dumps(data)), True)


if __name__ == "__main__":
    main()