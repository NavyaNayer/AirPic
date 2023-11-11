import tensorflow as tf

# Load the Keras model
model = tf.keras.models.load_model(
    "C:\\Users\\jaya2\\AndroidStudioProjects\\airpic\\Jayashre\\models\\SmileDetection\\vgg_model_ft_one.h5"
)

# Convert the model to TFLite format
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save the TFLite model to a file
with open(
    "C:\\Users\\jaya2\\AndroidStudioProjects\\airpic\\Jayashre\\models\\SmileDetection\\smile_detection_model.tflite",
    "wb",
) as f:
    f.write(tflite_model)
