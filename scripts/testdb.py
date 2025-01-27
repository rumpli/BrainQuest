import requests
import json
import time

base_url = "http://localhost:8080/api/"
sleep = 0.1

#
# Login
#

endpoint = "auth/login"
credentials = {
    "username": "brainquest",
    "password": "brainquest"
}

response = requests.post(base_url + endpoint, json=credentials)
access_token = response.json().get("accessToken")

headers = {"Authorization": f"Bearer {access_token}"}

#
# Populate Topics, Questions, and Answers
#

file_path = "./scripts/data/question_catalog_sample.json"

with open(file_path, "r", encoding="utf-8") as file:
    combined_data = json.load(file)

# Topics Endpoint
endpoint_topics = "topics"
# Questions Endpoint
endpoint_questions = "questions"
# Answers Endpoint
endpoint_answers = "answers"

print("Populating API with Topics, Questions, and Answers:")

for topic in combined_data:
    # Add topic
    response = requests.post(base_url + endpoint_topics, json={"name": topic["name"], "description": topic["description"]}, headers=headers)
    print(f"Topic Response: {response.status_code} - {topic['name']}")
    time.sleep(sleep)

    if response.status_code == 201 or response.status_code == 200:
        topic_id = response.json().get("id")

        for difficulty, questions in topic["questions"].items():
            for question in questions:
                # Add question
                question_data = {
                    "question": question["question"],
                    "info": question["info"],
                    "difficulty": difficulty.upper(),
                    "topic": {"id": topic_id}
                }
                response = requests.post(base_url + endpoint_questions, json=question_data, headers=headers)
                print(f"Question Response: {response.status_code} - {question['question']}")
                time.sleep(sleep)

                if response.status_code == 201 or response.status_code == 200:
                    question_id = response.json().get("id")

                    for answer in question["answers"]:
                        # Add answer
                        answer_data = {
                            "answer": answer["answer"],
                            "correct": answer["correct"],
                            "question": {"id": question_id}
                        }
                        response = requests.post(base_url + endpoint_answers, json=answer_data, headers=headers)
                        print(f"Answer Response: {response.status_code} - {answer['answer']}")
                        time.sleep(sleep)

print("API population complete.")
