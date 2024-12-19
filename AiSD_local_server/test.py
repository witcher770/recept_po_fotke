import requests
from PIL import Image

# URL вашего сервера
# url = "http://92.53.120.84:25/process_text"  # Замените "/process" на реальный путь, если он другой
# url_text = "http://127.0.0.1:5000/process_text"  # Замените "/process" на реальный путь, если он другой
url_text = "http://192.168.0.116:5000/process_text"  # Замените "/process" на реальный путь, если он другой
url_image = "http://127.0.0.1:5000/process_image"  # Замените "/process" на реальный путь, если он другой
# Укажите нужный endpoint
# import sys
# print(sys.executable)
# Текст, который нужно отправить
text_to_send = "СhРАЧ"

# Подготовка данных в формате JSON
data = {"text": text_to_send}



# Путь к изображению на ПК или в папке проекта
# image_path = 'C:\\Users\\Maksim_Misha\\Downloads\\three_apples.jpg'
image_path = 'product_images//apple_carrot_salat.jpg'

# Открытие изображения и сохранение его в переменную
image_to_send = Image.open(image_path)

# Вы можете выполнять с этим изображением различные операции, например, показать его
# image_to_send.show()

data_image = {"image": image_to_send}

try:
    flag = int(input(f"1. Отправить текст "
                       f"\n2. Отправить фото"
                       f"\n3. Выйти"
                       f"\n Выберите что нужно сделать: \n"))

    while flag != 3:
        if flag == 1:
            # # Отправка GET-запроса на сервер с параметром в URL
            # response = requests.get(url, params={"text": text_to_send})
            response = requests.post(url_text, json=data)
            # Проверка статуса ответа
            print("Код статуса:", response.status_code)
            # print("Ответ от сервера:", response.text) # Печатает весь текст ответа от сервера

            # Попытка прочитать JSON, если сервер отправляет его
            if response.status_code == 200:
                try:
                    processed_text = response.json().get("processedText")  # Извлекаем обработанный текст
                    print("Обработанный текст:", processed_text)
                except ValueError:
                    print("Ошибка: Сервер не вернул JSON.")
            else:
                print("Ошибка на сервере:", response.status_code, response.text)

        if flag == 2:
            # Открываем изображение и отправляем его как файл
            with open(image_path, 'rb') as image_file:
                files = {'image': image_file}
                response = requests.post(url_image, files=files)

            # Проверка статуса ответа
            print("Код статуса:", response.status_code)
            # print("Ответ от сервера:", response.что-то)

            # Попытка прочитать JSON, если сервер отправляет его
            if response.status_code == 200:
                try:
                    processed_image = response.json().get("recipe")  # Извлекаем обработанные данные
                    print("Обработанный текст:", processed_image)
                except ValueError:
                    print("Ошибка: Сервер не вернул JSON.")
            else:
                print("Ошибка на сервере:", response.status_code, response.text)

        flag = int(input(f"\n1. Отправить текст "
                         f"\n2. Отправить фото"
                         f"\n3. Выйти"
                         f"\n Выберите что нужно сделать: \n"))

except requests.RequestException as e:
    print("Не удалось подключиться к серверу:", e)
