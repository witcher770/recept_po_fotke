# from flask import Flask, request, jsonify
# import torch
# import signal
# from PIL import Image
# import os
# from transformers import GPT2LMHeadModel, GPT2Tokenizer
# from torchvision import transforms
#
# app = Flask(__name__)
#
# # Загрузка модели для распознавания объектов (пример YOLOv5)
# model_detection = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)
#
# # print(torch.cuda.is_available())  # Выведет True, если доступен GPU
# # print(torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu'))  # Покажет, на каком устройстве работа>
# #device = torch.device("cpu")
# #model_detection = model_detection.to(device)
#
# # Загрузка языковой модели (GPT)
# model_recipe = GPT2LMHeadModel.from_pretrained("gpt2")
# tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
#
# # Функция для обработки тайм-аута
# def timeout_handler(signum, frame):
#     raise TimeoutError("Request timed out!")
#
# # Устанавливаем тайм-аут на 10 секунд
# signal.signal(signal.SIGALRM, timeout_handler)
#
# # Функция для распознавания продуктов
# def detect_products(image):
#     results = model_detection(image)
#     products = results.pandas().xyxy[0]['name'].tolist()  # Получаем список продуктов
#     return products
#
# # Функция для генерации рецептов
# def generate_recipe(products):
#     prompt = f"На основе продуктов {', '.join(products)}, придумайте рецепт блюда:"
#     inputs = tokenizer.encode(prompt, return_tensors="pt")
#     outputs = model_recipe.generate(inputs, max_length=150, num_return_sequences=1)
#     recipe = tokenizer.decode(outputs[0], skip_special_tokens=True)
#     return recipe
#
#
# @app.route('/process', methods=['POST'])
# def process_data():
#     # Получаем строку, отправленную с клиента
#     data = request.json.get('data')
#
#     # Изменяем строку
#     modified_data = data.upper()  # Например, превращаем в верхний регистр
#
#     # Возвращаем измененную строку обратно
#     return jsonify({'result': modified_data})
#
# @app.route('/process_image', methods=['POST'])
# def process_image():
#     if 'image' not in request.files:
#         return jsonify({"error": "Нет изображения в запросе"}), 400
#
#     file = request.files['image']
#     image = Image.open(file.stream)
#
#     # Распознаем продукты
#     products = detect_products(image)
#
#     # Генерируем рецепт
#     recipe = generate_recipe(products)
#
#     return jsonify({"products": products, "recipe": recipe})
#
# @app.route('/process_text', methods=['POST'])
# def process_text():
#     # Устанавливаем тайм-аут
#     # signal.alarm(10)  # Тайм-аут в 10 секунд
#     data = request.get_json()
#     if 'text' not in data:
#         return jsonify({'error': 'No text provided'}), 400
#     processed_text = "Все говорят " + data['text'] + ", а ты купи слона"  # Здесь выполняется обработка строки
#     return jsonify({'processedText': processed_text})
#
#
# @app.route('/upload_photo', methods=['POST'])
# def upload_photo():
#     if 'photo' not in request.files:
#         return jsonify({"error": "No photo uploaded"}), 400
#
#     photo = request.files['photo']
#     photo_path = os.path.join("uploads", photo.filename)
#     photo.save(photo_path)
#
#     # Пример обработки изображения (можно заменить на свою логику)
#     image = Image.open(photo_path)
#     width, height = image.size
#     result = [f"Width: {width}", f"Height: {height}"]
#
#     return jsonify(result)
#
#
# @app.route('/process_list', methods=['POST'])
# def process_list():
#     data = request.json
#     if not isinstance(data, list):
#         return jsonify({"error": "Invalid data format"}), 400
#
#     # Пример обработки списка
#     result = "! ".join(data)  # Объединяем список в строку
#     return jsonify(result)
#
#
#
# if __name__ == '__main__':
#     # app.run(host='92.53.120.84', port=25)
#     app.run(host='0.0.0.0')



from flask import Flask, request, jsonify
from PIL import Image
from transformers import GPT2LMHeadModel, GPT2Tokenizer

from ultralytics import YOLO


import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)


from ultralytics import YOLO
model_detection = YOLO('yolov8s.pt')  # Downloaded model file

# Загрузка моделей
# model_detection = YOLO('yolov5s')
model_recipe = GPT2LMHeadModel.from_pretrained("gpt2")
tokenizer = GPT2Tokenizer.from_pretrained("gpt2")


def detect_products(image):
    print(type(image))
    results: list = model_detection(image) # получаем продукты, изображенные на фото
    print(results)
    products = results.pandas().xyxy[0]['name'].dropna().tolist()
    return products


def generate_recipe(products):
    prompt = f"На основе продуктов {', '.join(products)}, придумайте рецепт блюда:"
    inputs = tokenizer.encode(prompt, return_tensors="pt")
    outputs = model_recipe.generate(inputs, max_length=150, temperature=0.7, top_k=50)
    recipe = tokenizer.decode(outputs[0], skip_special_tokens=True)
    return recipe


@app.route('/process_image', methods=['POST'])
def process_image():
    if 'image' not in request.files:
        return jsonify({"error": "Нет изображения в запросе"}), 400

    file = request.files['image']
    image = Image.open(file.stream).convert("RGB")

    try:
        products = detect_products(image)
        recipe = generate_recipe(products)
        return jsonify({"products": products, "recipe": recipe})
    except Exception as e:
        logging.error(f"Ошибка обработки запроса: {e}")
        return jsonify({"error": str(e)}), 500


@app.route('/process_text', methods=['POST'])
def process_text():
    # Устанавливаем тайм-аут
    # signal.alarm(10)  # Тайм-аут в 10 секунд
    data = request.get_json()
    if 'text' not in data:
        return jsonify({'error': 'No text provided'}), 400
    processed_text = "Все говорят " + data['text'] + ", а ты купи слона"  # Здесь выполняется обработка строки
    return jsonify({'processedText': processed_text})


if __name__ == "__main__":
    app.run(host='0.0.0.0')


