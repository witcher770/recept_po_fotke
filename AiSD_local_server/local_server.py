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
from transformers import AutoModelForCausalLM, AutoTokenizer
import logging
from ultralytics import YOLO


app = Flask(__name__)
logging.basicConfig(level=logging.INFO)


# Загрузка модели и токенизатора
# model_name = "tiiuae/falcon-7b-instruct"  # Или другая подходящая модель
# tokenizer = AutoTokenizer.from_pretrained(model_name)
# model = AutoModelForCausalLM.from_pretrained(model_name, device_map="auto", torch_dtype="float16")
# model_name = "meta-llama/Llama-2-7b-chat-hf"
# access_token = "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"  # Ваш токен доступа

# tokenizer = AutoTokenizer.from_pretrained(model_name, use_auth_token=access_token)
# model = AutoModelForCausalLM.from_pretrained(model_name, use_auth_token=access_token, device_map="auto", torch_dtype="float16")
# tokenizer.pad_token = tokenizer.eos_token
# model.config.pad_token_id = tokenizer.eos_token_id


model_detection = YOLO('yolov5su.pt')  # Downloaded model file

# Загрузка моделей
# model_detection = YOLO('yolov5s')
model_recipe = GPT2LMHeadModel.from_pretrained("gpt2")
tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
tokenizer.pad_token = tokenizer.eos_token
model_recipe.config.pad_token_id = tokenizer.eos_token_id


def detect_products(image):
    results: list = model_detection(image)  # получаем продукты, изображенные на фото
    products = []
    for result in results:
        for box in result.boxes:
            class_name = model_detection.names[box.cls.cpu().item()]  # Имя класса
            products.append(class_name)
    return products


# def generate_recipe(products):
#     # prompt = f"На основе продуктов {', '.join(products)}, придумайте рецепт блюда:"
#     # prompt = f"Based on the products {', '.join(products)}, come up with a recipe for a dish:"
#     prompt = (f"Based on the following products: {', '.join(products)}, create a detailed recipe with ingredients and "
#               f"cooking instructions.")
#     # prompt = f"""Based on the following products: {', '.join(products)}, create a detailed recipe."""
#
#
#
#     inputs = tokenizer.encode(prompt, return_tensors="pt", padding=True, truncation=True)  #
#     attention_mask = inputs.ne(tokenizer.pad_token_id).long()  # Генерация маски на основе идентификатора заполнителя
#     # outputs = model_recipe.generate(
#     #     inputs,
#     #     attention_mask=attention_mask,
#     #     max_length=400,
#     #     temperature=1.0,
#     #     top_k=50,
#     #     top_p=0.9,
#     #     do_sample=True
#     # )
#     # Генерация текста
#     # outputs = model_recipe.generate(
#     #     inputs,
#     #     attention_mask=attention_mask,
#     #     max_length=300,  # Умеренная длина
#     #     temperature=0.8,  # Сбалансированная случайность
#     #     top_k=50,  # Оставляем только 50 лучших вариантов
#     #     top_p=0.95,  # Ограничиваем вероятности до 95%
#     #     do_sample=True,  # Используем выборку
#     #     repetition_penalty=1.2,  # Предотвращаем повторы
#     # )
#
#     outputs = model_recipe.generate(
#         inputs,
#         attention_mask=attention_mask,
#         max_length=300,  # Ограничиваем длину
#         temperature=0.7,  # Уменьшаем случайность
#         top_k=40,  # Оставляем топ-40 токенов
#         top_p=0.9,  # Ограничиваем вероятность
#         do_sample=True,  # Используем выборку
#         repetition_penalty=1.5,  # Штраф за повторения
#     )
#
#     # print(outputs)
#     recipe = tokenizer.decode(outputs[0], skip_special_tokens=True)
#     print(recipe)
#     return recipe

# def generate_recipe(products):
#     # Четкий и лаконичный запрос
#     prompt = f"""Based on the following products: {', '.join(products)}, create a detailed recipe.
#     Respond strictly in this format:
#     1. Ingredients:
#     - (list the ingredients here)
#     2. Instructions:
#     - (list step-by-step instructions here)
#     """
#
#     # Токенизация
#     # inputs = tokenizer(prompt, return_tensors="pt", padding=True, truncation=True)
#     inputs = tokenizer.encode(prompt, return_tensors="pt", padding=True, truncation=True)
#     attention_mask = inputs.ne(tokenizer.pad_token_id).long()  # Генерация маски на основе идентификатора заполнителя
#     # Генерация текста
#     outputs = model_recipe.generate(
#         # inputs["input_ids"],
#         inputs,
#         attention_mask=attention_mask,
#         max_length=300,  # Максимальная длина текста
#         temperature=0.7,  # Умеренная случайность
#         top_k=50,  # Оставляем только 50 лучших вариантов
#         top_p=0.9,  # Ограничиваем вероятности до 90%
#         repetition_penalty=1.5,  # Предотвращаем повторения
#     )
#
#     # Декодирование и возвращение результата
#     recipe = tokenizer.decode(outputs[0], skip_special_tokens=True)
#
#     # Постобработка для проверки формата
#     # formatted_recipe = []
#     # for line in recipe.split("\n"):
#     #     if line.startswith("1.") or line.startswith("2."):
#     #         formatted_recipe.append(line)
#     #
#     # return "\n".join(formatted_recipe)
#     return recipe


def generate_recipe(products):
    prompt = f"На основе продуктов {', '.join(products)}, придумайте рецепт блюда:"
    inputs = tokenizer.encode(prompt, return_tensors="pt", padding=True, truncation=True)
    attention_mask = inputs.ne(tokenizer.pad_token_id).long()  # Генерация маски на основе идентификатора заполнителя
    outputs = model_recipe.generate(
        inputs,
        attention_mask=attention_mask,
        max_length=400,
        temperature=0.7,
        top_k=50,
        top_p=0.9,  # Ограничиваем вероятности до 90%
        repetition_penalty=1.5  # Предотвращаем повторения
    )
    print(outputs)
    recipe = tokenizer.decode(outputs[0], skip_special_tokens=True)
    print(recipe)
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
    app.run(host='192.168.0.116')


