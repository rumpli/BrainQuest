# testdb.py

> For now everything ist hard coded and has some limitation:
> - only works properly when executed once without errors (all resonse code 200)
> - works only on local development environment

This script adds some demo data (topic, questions, answers) to the BrainQuest App.

It is reading the JSON file from the folder `sample_data` and adds them over the REST API.

**Run script**

`python3 testdb.py`