# ML Predict API

## 1. Project Introduction

This project implements a full-stack solution for behavior prediction based on event data. The system contains three parts:

- **Model Training**: A Jupyter Notebook (`model_training.ipynb`) processes a dataset (`events.csv`) and trains a classification model. The model is saved in ONNX format.
- **Java REST API**: A Java-based API loads the ONNX model and exposes a prediction endpoint.
- **Dockerized Deployment**: The API is containerized using Docker and served over HTTPS on port `443`.

## 2. Environment Setup & Startup Instructions

### A. Train the Model

1. Navigate to the `model-dev` directory:

   ```bash
   cd path/to/model-dev
   ```

2. Download the Retailrocket Recommender System Dataset from Kaggle, and ensure `events.csv` and `model_training.ipynb` are in the same folder.

3. Open the Jupyter Notebook and run all cells:

   ```bash
   jupyter notebook model_training.ipynb
   ```

4. This will export a trained model as `behavior_model.onnx`.

### B. Build the Java API

1. Navigate to the `api-java` directory:

   ```bash
   cd path/to/api-java
   ```

2. Use Maven to build the project and generate the JAR file:

   ```bash
   ./mvnw clean package
   ```

3. This will produce `target/behavior-api-1.0.0.jar`, which will be used by Docker.

### C. Dockerize and Deploy

1. Move or copy `target/behavior-api-1.0.0.jar` to the `docker` directory:

2. Navigate to the `docker` directory:

   ```bash
   cd path/to/docker
   ```

3. Build the Docker image:

   ```bash
   docker build -t behavior-api .
   ```

4. Run the container with HTTPS enabled:

   ```bash
   docker run -d --name behavior-api-container -p 443:443 --cap-add=NET_BIND_SERVICE behavior-api
   ```

5. Test the API locally:

   ```bash
   curl -k -X POST https://localhost/predict_behavior \
     -H "Content-Type: application/json" \
     -d '{"sequence": [0, 1, 2, 0, 1]}'
   ```

## 3. API Documentation

- **Endpoint**: `POST /predict_behavior`
- **Request Body** (JSON):

  ```json
  {
    "sequence": [0, 1, 2, 0, 1]
  }
  ```

- **Response Body**:

  ```json
  {
    "predicted_class": 2,
    "confidence": 2.10
  }
  ```

## 4. Git Branches Explanation

This project uses four Git branches:

- `main`: Contains the README.
- `model-dev`: Contains the notebook for model training (`model_training.ipynb`).
- `api-java`: Contains the Java source code and `pom.xml`.
- `docker`: Contains the `Dockerfile`.

## 5. Author & Development Notes

**Author**: Liyuan Hou

**Development Notes**:

- This project is an end-to-end integration of model training, API deployment, and secure containerization.
- A key challenge was resolving runtime errors from Docker due to incorrect .jar packaging. The JAR must be executable and placed in the same directory as the Dockerfile when building the image.