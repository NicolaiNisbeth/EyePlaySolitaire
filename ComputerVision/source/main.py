
import sys
import json
import time
import base64
import io
import cv2

from PIL import Image

from message import Message
from connector import Connector
import camera


def loadImage(image):
    img = Image.open("C:/Users/malte/IdeaProjects/EyePlaySolitaire/ComputerVision/"+image)    
    imgByteArr = io.BytesIO()
    img.save(imgByteArr, format='PNG')
    img_data = imgByteArr.getvalue()
    img_data_encoded = base64.b64encode(img_data)
    return img_data_encoded



# images = [
#     loadImage("testimage1.png"),
#     loadImage("testimage2.jpg"),
#     loadImage("testimage3.jpg")
# ]

connector = None

def main():
    global connector

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
    
    
    camera.start_camera()
    print("Started camera")

    # f =  open("C:/Users/malte/IdeaProjects/EyePlaySolitaire/ComputerVision/testfile.txt", "r")
    # data = f.read()
    # f.close()
    # len(data)

    #connector.send_message(Message(100, json.dumps({"msg" : data})), True)
    #print("Sent first message")

    # connector.send_message(Message(101, json.dumps({"num" : 19})), True)
    time.sleep(5)
    send_image()
    
    

def message_received(msg: Message):
    print("Received message with code ", msg._code)
    if msg._code is 201:
        send_image()
    



image_counter = 0

def send_image():
    global connector
    image = camera.get_current_frame()
    if image is not None:
        connector.send_message(Message(102, json.dumps({"image": image.decode('ascii'), "width":1280, "height":720})), True)
        print(f"Sent {len(image)} bytes")
    else:
        print("WARNING: Image was None!")    

if __name__ == "__main__":
    main()