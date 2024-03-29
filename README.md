
<h1><img align="center" height="40" src="https://cdn0.iconfinder.com/data/icons/ios-web-user-interface-3d-square-vol-4/96/Checklist_clipboard_inventory_list_report_tasks_todo-1024.png"> Java Task Tracker</h1>

## Description 👨‍🏫

Приложение реализует трекер задач.
Всего в приложении 3 вида задач: задача, эпик, подзадача.

Задачей считается какое-либо конкретное действие: купить продукты, заплатить за интернет и т.д.

Эпик - большая задача, которая выполняется в несколько шагов - подзадач.

Для каждого вида задач предусмотрены следующие действия:
1) Получение конкретной задачи по id 
2) Получение всех задач 
3) Добавление задачи
4) Обновление задачи
5) Удаление задачи по id 
6) Удаление всех задач
7) Получение списка приоритетных задач по времени
8) Получение истории из 10 последних просмотренных задач

Реализовано хранение данных:
1) В файле 
2) На сервере (KVServer)

## Tech Stack 🔧

Java Core 11, JUnit 5, Gson, HttpServer, KVServer

## API Reference ⚙️
- GET tasks/ - получить список приоритетных задач
- GET tasks/history - получить историю из 10 последних просмотренных задач
- GET subtask/epic/{id} - получить список подзадач конкретного эпика

- GET tasks/task - получить список всех задач
- GET tasks/task/{id} - получить задачу по id 
- POST tasks/task - создать задачу 
- POST tasks/task/{id} - обновить задачу
- DELETE tasks/task - удалить все задачи
- DELETE tasks/task/{id} - удалить задачу по id
*аналогичные эндпоинты для эпиков и подзадач

## How to start the project ▶️

### 1) Склонируйте репозиторий и перейдите в него 
```
git clone https://github.com/Stepashka37/java-kanban.git
```
### 2) Запустите проект в выбранной IDE

### 3) Перейдите по адресу 
```
http://localhost:8080/tasks
```
### 4) Можно работать с проектом 


## Tests 📑
Тестирование выполнено с помощью фреймворка JUnit 5.

Ознакомиться с результатами тестов можно по ссылке: http://localhost:63342/java-kanban/Test%20Results%20-%20All_in_java-kanban.html?_ijt=cbtg2goa1tn6st9385laaveupp&_ij_reload=RELOAD_ON_SAVE

*ссылка действует при запущенном на вашем ПК проекте





