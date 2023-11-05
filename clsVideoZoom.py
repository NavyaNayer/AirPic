import mediapipe as mp
import cv2
import time
import math
import imutils
import numpy as np

class clsVideoZoom():
    def __init__(self):
        self.minVal = 0.01
        self.maxVal = 1

    def zoomVideo(self, image, Iscale=1):
        try:
            scale = Iscale

            # Get the image size
            height, width, channels = image.shape

            # Prepare the crop
            centerX, centerY = int(height / 2), int(width / 2)
            radiusX, radiusY = int(scale * centerX), int(scale * centerY)

            minX, maxX = centerX - radiusX, centerX + radiusX
            minY, maxY = centerY - radiusY, centerY + radiusY

            cropped = image[minX:maxX, minY:maxY]
            resized_cropped = cv2.resize(cropped, (width, height))

            return resized_cropped

        except Exception as e:
            print('Error:', str(e))
            return image

    def runSensor(self):
        try:
            pTime = 0
            cap = cv2.VideoCapture(0)
            detector = mp.solutions.hands.Hands(static_image_mode=False, max_num_hands=1, min_detection_confidence=0.7)
            draw_util = mp.solutions.drawing_utils

            while True:
                success, img = cap.read()
                img = imutils.resize(img, width=720)
                img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
                results = detector.process(img_rgb)

                zRange = 0  # Initialize zRange here
                minVal = self.minVal  # Initialize minVal here
                maxVal = self.maxVal  # Initialize maxVal here

                if results.multi_hand_landmarks:
                    for hand_landmarks in results.multi_hand_landmarks:
                        x1, y1 = int(hand_landmarks.landmark[4].x * img.shape[1]), int(hand_landmarks.landmark[4].y * img.shape[0])
                        x2, y2 = int(hand_landmarks.landmark[8].x * img.shape[1]), int(hand_landmarks.landmark[8].y * img.shape[0])
                        cx, cy = (x1 + x2) // 2, (y1 + y2) // 2

                        cv2.circle(img, (x1, y1), 15, (255, 0, 255), cv2.FILLED)
                        cv2.circle(img, (x2, y2), 15, (255, 0, 255), cv2.FILLED)
                        cv2.line(img, (x1, y1), (x2, y2), (255, 0, 255), 3)
                        cv2.circle(img, (cx, cy), 15, (255, 0, 255), cv2.FILLED)

                        lenVal = math.hypot(x2 - x1, y2 - y1)
                        print('Length:', str(lenVal))

                        # Hand Range is from 50 to 270
                        # Camera Zoom Range is 0.01 to 1

                        zRange = np.interp(lenVal, [50, 270], [minVal, maxVal])

                        print('Range:', str(zRange))

                        if lenVal < 50:
                            cv2.circle(img, (cx, cy), 15, (0, 255, 0), cv2.FILLED)

                rangeBar = np.interp(zRange, [minVal, maxVal], [400, 150])

                cv2.rectangle(img, (50, 150), (85, 400), (255, 0, 0), 3)
                cv2.rectangle(img, (50, int(rangeBar)), (85, 400), (255, 0, 0), cv2.FILLED)

                cTime = time.time()
                fps = 1 / (cTime - pTime)
                pTime = cTime

                cv2.putText(img, str(int(fps)), (10, 70), cv2.FONT_HERSHEY_PLAIN, 3, (255, 0, 255), 3)
                cv2.imshow("Original Source", img)

                # Creating the new zoomed video
                cropImg = self.zoomVideo(img, zRange)
                cv2.putText(cropImg, str(int(fps)), (10, 70), cv2.FONT_HERSHEY_PLAIN, 3, (255, 0, 255), 3)
                cv2.imshow("Zoomed Source", cropImg)

                if cv2.waitKey(1) == ord('q'):
                    break

            cap.release()
            cv2.destroyAllWindows()

            return 0
        except Exception as e:
            print('Error:', str(e))
            return 1

if __name__ == '__main__':
    videoZoom = clsVideoZoom()
    videoZoom.runSensor()
