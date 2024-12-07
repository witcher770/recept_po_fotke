import tensorflow as tf
from object_detection.utils import config_util
from object_detection.protos import pipeline_pb2
from google.protobuf import text_format
import os
import pathlib

# Загрузим модель
MODEL_NAME = 'ssd_mobilenet_v2_fpnlite_320x320_coco17_tpu-8'  # Модель COCO с продуктами питания
PATH_TO_CFG = 'models/' + MODEL_NAME + '/pipeline.config'
PATH_TO_CKPT = 'models/' + MODEL_NAME + '/checkpoint'

# Загрузка конфигурации
pipeline_config = pipeline_pb2.TrainEvalPipelineConfig()
with tf.io.gfile.GFile(PATH_TO_CFG, "r") as f:
    proto_str = f.read()
    text_format.Merge(proto_str, pipeline_config)

# Загрузка чекпоинта модели
model_dir = pathlib.Path(PATH_TO_CKPT)
ckpt = tf.compat.v2.train.Checkpoint(model=model_dir)
ckpt.restore(model_dir)

# Модель готова к использованию
import cv2
import numpy as np

# Загрузка изображения
image_path = 'product_images//one_apple.jpg'
image_np = np.array(cv2.imread(image_path))

# Подготовка изображения
input_tensor = np.expand_dims(image_np, 0)

# Прогон через модель
model_fn = model_dir.load()
model_fn.eval()
output_dict = model_fn(input_tensor)

# Пример распознавания и вывода объектов
for idx, box in enumerate(output_dict['detection_boxes']):
    print(f"Object {idx + 1}:")
    print(f"Box: {box}")
    print(f"Score: {output_dict['detection_scores'][idx]}")
    print(f"Class: {output_dict['detection_classes'][idx]}")
