
# Make import available to us

import cv2
import sys, os
sys.path.append(os.path.abspath(os.path.join('..', 'source')))

from detector import Detector
import detector
import numpy as np


THRESHOLD = 0.01


class Result:

    def __init__(self, test_name):
        self.test_name = test_name
        self.expected_total = 0
        self.detected_total = 0
        self.correct = []
        self.missing = []
        self.wrong = []
        self.unexpected_card = []
        self.unexpected_placement = []

    def combine(self, other):
        self.expected_total = self.expected_total + other.expected_total
        self.detected_total = self.detected_total + other.detected_total
        self.correct = self.correct + other.correct
        self.missing = self.missing + other.missing
        self.wrong = self.wrong + other.wrong
        self.unexpected_card = self.unexpected_card + other.unexpected_card
        self.unexpected_placement = self.unexpected_placement + other.unexpected_placement


    def print(self):
        print(self.test_name)
        print(f"\tExpected: {self.expected_total}   Detected: {self.detected_total}")
        print(f"\tCorrect:  {len(self.correct)/self.expected_total*100:.1f}%  (Conf.: {np.mean([c[1] for c in self.correct])*100:.1f}%) ")
        print(f"\tWrong:  {len(self.wrong)/self.expected_total*100:.1f}%  (Conf.: {np.mean([c[2] for c in self.wrong])*100:.1f}%) ")
        print(f"\tMissing:  {len(self.missing)/self.expected_total*100:.1f}%")
        print(f"\tUnexpected card:  {len(self.unexpected_card)/self.detected_total*100:.1f}% of detected  (Conf.: {np.mean([c[1] for c in self.unexpected_card])*100:.1f}%) ")
        print(f"\tUnexpected placement:  {len(self.unexpected_placement)/self.detected_total*100:.1f}% of detected  (Conf.: {np.mean([c[1] for c in self.unexpected_placement])*100:.1f}%) ")


    def add_correct(self, label, confidence):
        self.correct.append((label, confidence))
        
    def add_missing(self, label):
        self.missing.append((label))
    
    def add_wrong(self, expected_label, detected_label, confidence):
        self.wrong.append((expected_label, detected_label, confidence))
    
    def add_unexpected_card(self, label, confidence):
        self.unexpected_card.append((label, confidence))

    def add_unexpected_placement(self, label, confidence):
        self.unexpected_placement.append((label, confidence))


# Cleans the raw output detections, by adjusting placement of values in object
# and normalizing the coordinates
def clean_detections(raw_detections, resolution):
    cleaned_detections = []
    for detection in raw_detections:
        cleaned_detection = (
            detection[0].decode("utf-8"), # Label
            detection[2][0] / resolution[0], # center y
            detection[2][1] / resolution[1], # center x
            detection[2][2] / resolution[0], # Width
            detection[2][3] / resolution[1], # Height
            detection[1] # Confidence
        )
        cleaned_detections.append(cleaned_detection)
    return cleaned_detections



def test(name, detector, image, expected):

    # Perform detections
    raw_detections = detector._detect(image)
    cleaned_detections = clean_detections(raw_detections, detector._darknet_resolution)

    result_set = Result(name)
    result_set.detected_total = len(cleaned_detections)
    result_set.expected_total = len(expected)

    for expected_detection in expected:
        
        test_expected(result_set, expected_detection, cleaned_detections)
        # if not detected:
        #     missing = missing + 1
        #     missing_list.append(expected_detection[0])

    # Check unexpected detections
    for cleaned_detection in cleaned_detections:
        card_exists = False
        for e in expected:
            if e[0] == cleaned_detection[0]:
                card_exists = True
                break

        if card_exists:
            result_set.add_unexpected_placement(cleaned_detection[0], cleaned_detection[1])
        else:
            result_set.add_unexpected_card(cleaned_detection[0], cleaned_detection[1])

    return result_set



# Check if the detection exists within the list, and in that
# case it remove the detection and returns True
def test_expected(result_set, expected_detection, detections):
    matches = []

    for i, detection in enumerate(detections.copy()):

        # Test threshold
        if expected_detection[1] <= detection[1]+THRESHOLD and expected_detection[1] >= detection[1]-THRESHOLD:
            if expected_detection[2] <= detection[2]+THRESHOLD and expected_detection[2] >= detection[2]-THRESHOLD:
                matches.append((i, detection))
            

    if len(matches) > 0:

        # Find the match closest to the expected detection
        bestDistance = 1
        bestMatch = None
        for match in matches:
            distance = (match[1][1]-expected_detection[1])**2 + (match[1][2]-expected_detection[2])**2
            if bestMatch is None or bestDistance > distance:
                bestDistance = distance
                bestMatch = match


        # Test label
        if expected_detection[0] == bestMatch[1][0]:
            result_set.add_correct(expected_detection[0], bestMatch[1][5])
        else:
            result_set.add_wrong(expected_detection[0], bestMatch[1][0], bestMatch[1][5])

        del detections[bestMatch[0]]
        
    else:
        result_set.add_missing(expected_detection[0])
    


# Load test file (expected results and image as cv image)
def load_test(suite, test_name, label_names):
    expected_detections = []

    with open(test_name +".txt") as test_file:
        lines = test_file.read().splitlines()
        for line in lines:
            tokens = line.split(" ")
            expected_detections.append((
                label_names[int(tokens[0])], # Label
                float(tokens[1]),  # X
                float(tokens[2]),  # Y
                float(tokens[3]),  # Width
                float(tokens[4]),  # Height
            ))

    image = cv2.imread(test_name + ".jpg")

    return (expected_detections, image)
    


# def display_result(img, d):

#     detections = d._detect(image)
#     scaledDetections = detector._scale_detections(detections, 1920/416, 1080/416)
#     new_image = detector.cvDrawBoxes(scaledDetections, img)

#     cv2.imshow('image', new_image)
#     cv2.waitKey(0)
#     cv2.destroyAllWindows()


all_suite_results = []

def test_suite(full_path, suite_name, label_names, det, root=False):
    suite_result = Result(os.path.join(full_path, suite_name))

    os.chdir(suite_name)
    for f in os.listdir():
        if os.path.isdir(f):
            result = test_suite(os.path.join(full_path, suite_name), f, label_names, det)
            suite_result.combine(result)

    if not root:
        test_names = []
        for f in os.listdir():
            if f[-4:] == ".jpg":
                test_names.append(f[0:-4])

        for i, test_name in enumerate(test_names):
            print(f"\n[{i}]\t", end="")
            expected_detections, image = load_test(suite_name, test_name, label_names)
            result = test(test_name, det, image, expected_detections)
            result.print()
            suite_result.combine(result)

        all_suite_results.append(suite_result)

        print("\n~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ")
        suite_result.print()
        print("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ")

    os.chdir("..")
    return suite_result


if __name__ == "__main__":

    suite_root = "all_suites"
    if len(sys.argv) == 2:
        suite_root = str(sys.argv[1])


    print("\nSTARTING TEST")
    print("Suite root:  ", suite_root)
    print("")

    # Label names from obj.names as list
    label_names = open(os.path.join("..", "input", "obj.names")).read().splitlines()

    d = Detector(None)

    result = Result(suite_root)
    result.combine(test_suite("", suite_root, label_names, d, root=True))
    
    print("\n\n============================================================")
    print("\nSUMMARY")

    for suite in all_suite_results:
        print("\n~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~\n")
        suite.print()

    print("\n============================================================")
    result.print()