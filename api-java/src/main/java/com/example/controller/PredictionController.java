// Controller Placeholder
package com.example.controller;

import ai.onnxruntime.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PredictionController {
    OrtEnvironment env;
    OrtSession session;

    public PredictionController() throws Exception {
        env = OrtEnvironment.getEnvironment();
        session = env.createSession("models/behavior_model.onnx", new OrtSession.SessionOptions());
    }

    @PostMapping("/predict_behavior")
    public Map<String, Object> predict(@RequestBody Map<String, Object> input) throws OrtException {
        List<Integer> seq = (List<Integer>) input.get("sequence");

        long[][] inputArr = new long[1][seq.size()];
        for (int i = 0; i < seq.size(); i++) {
            inputArr[0][i] = seq.get(i);
        }

        OnnxTensor tensor = OnnxTensor.createTensor(env, inputArr);
        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input", tensor);

        OrtSession.Result result = session.run(inputs);
        float[][] output = (float[][]) result.get(0).getValue();

        int bestClass = 0;
        float maxProb = output[0][0];
        for (int i = 1; i < output[0].length; i++) {
            if (output[0][i] > maxProb) {
                bestClass = i;
                maxProb = output[0][i];
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("predicted_class", bestClass);
        response.put("confidence", maxProb);
        return response;
    }
}