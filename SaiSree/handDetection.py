import cv2
import mediapipe as mp

# Create a VideoCapture object to capture video from the camera
cap = cv2.VideoCapture(0)

# Check if the camera is opened successfully
if not cap.isOpened():
    print("Unable to open the camera.")
    exit()

# Initialize Mediapipe Hands module
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=False, max_num_hands=1, min_detection_confidence=0.5)

# Counter to track captured images
image_counter = 0

while True:
    # Read a frame from the camera
    ret, frame = cap.read()

    # If the frame was not captured successfully, exit the loop
    if not ret:
        break

    # Convert the frame to RGB format (Mediapipe requires RGB input)
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    # Process the frame with Mediapipe Hands
    results = hands.process(rgb_frame)

    # Check if hands are detected
    if results.multi_hand_landmarks:
        for hand_landmarks in results.multi_hand_landmarks:
            # Draw hand landmarks on the frame
            for idx, landmark in enumerate(hand_landmarks.landmark):
                h, w, c = frame.shape
                cx, cy = int(landmark.x * w), int(landmark.y * h)
                cv2.circle(frame, (cx, cy), 5, (0, 255, 0), -1)

            # Capture the image when a hand is detected
            image_name = f"detected_hand_{image_counter}.png"
            cv2.imwrite(image_name, frame)
            image_counter += 1

    # Display the result
    cv2.imshow('Hand Detection', frame)

    # Exit the loop when 'q' is pressed
    if cv2.waitKey(1) == ord('q'):
        break

# Release the VideoCapture object and close the windows
cap.release()
cv2.destroyAllWindows()
