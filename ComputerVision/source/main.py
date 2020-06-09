
import sys
import json
import time
import base64
import io
from PIL import Image

from message import Message
from connector import Connector


def loadImage(image):
    img = Image.open("C:/Users/malte/IdeaProjects/EyePlaySolitaire/ComputerVision/"+image)    
    imgByteArr = io.BytesIO()
    img.save(imgByteArr, format='PNG')
    img_data = imgByteArr.getvalue()
    img_data_encoded = base64.b64encode(img_data)
    return img_data_encoded



images = [
    loadImage("testimage1.png"),
    loadImage("testimage2.jpg"),
    loadImage("testimage3.jpg")
]

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
        raise TypeError("Port argument but be integer larger between 1 and 65535")

    connector = Connector(port, message_received)
    # f =  open("C:/Users/malte/IdeaProjects/EyePlaySolitaire/ComputerVision/testfile.txt", "r")
    # data = f.read()
    # f.close()
    # len(data)

    #connector.send_message(Message(100, json.dumps({"msg" : data})), True)
    #print("Sent first message")

    # connector.send_message(Message(101, json.dumps({"num" : 19})), True)
    send_image()
    
    # print("Sent third message")

    time.sleep(200)
    
    

def message_received(msg: Message):
    print("Received message with code ", msg._code)
    if msg._code is 201:
        send_image()
    



image_counter = 0

def send_image():
    global images
    global image_counter
    global connector
    connector.send_message(Message(102, json.dumps({"image" : images[image_counter].decode('ascii')})), True)
    print("Sent image ", image_counter)
    image_counter = (image_counter+1) % len(images)

if __name__ == "__main__":
    main()