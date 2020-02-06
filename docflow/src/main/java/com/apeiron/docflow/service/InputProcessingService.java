package com.apeiron.docflow.service;

import com.apeiron.docflow.domain.InputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class InputProcessingService {
    private final Logger log = LoggerFactory.getLogger(InputProcessingService.class);
    private final String CAPITAL = "input_capital_social_de_la_societé";
    private final String AMMOUNT_ASSOCIATES_SHARES_PREFIX = "input_nombre_de_parts_sociales_";
    private final String PV_NOMINATION_NOUVEAU_GERANT_LEGALDOCUMENT_ID = "5dde43ffd02a3b36803e18b8";


    public Map<String, InputData> refactorInput(Map<String, InputData> stepperData) {

        if (stepperData != null && stepperData.containsKey("LEGAL_DOCUMENT_ID")
            && PV_NOMINATION_NOUVEAU_GERANT_LEGALDOCUMENT_ID.equals(stepperData.get("LEGAL_DOCUMENT_ID").getValue())) {
            stepperData = calculateAmountAssociatesShares(stepperData);
        }
        return stepperData;
    }

    /**
     * Method for calculate each partner's share
     *
     * @param stepperData list of bookmarks with value from the stepper data
     * @return the stepperData with the new value of associate part
     */
    private Map<String, InputData> calculateAmountAssociatesShares(Map<String, InputData> stepperData) {
        log.debug("Request to calculate the associate part with stepper data : {}", stepperData);

        Map<String, InputData> mapAssociatePart = new HashMap<>();
        stepperData.forEach((key, value) -> {
            if (key != null && !key.isEmpty() && (key.startsWith(AMMOUNT_ASSOCIATES_SHARES_PREFIX) || key.startsWith(CAPITAL))) {
                mapAssociatePart.put(key, value);
            }
        });
        Double capital = Double.valueOf(mapAssociatePart.get(CAPITAL) !=null ? mapAssociatePart.get(CAPITAL).getValue() : "0");
        Double sumOfParts = 0.0;

        for (Map.Entry<String, InputData> entry : mapAssociatePart.entrySet()) {
            if (!entry.getKey().equals(CAPITAL) && entry.getValue()!=null) {

                    sumOfParts += Double.valueOf(entry.getValue().getValue());

            }
        }
        Double partPrice = 0.0;
        if (capital != null && sumOfParts > 0) {
            partPrice = capital / sumOfParts;
        }

        for (Map.Entry<String, InputData> entry : mapAssociatePart.entrySet()) {
            if (!entry.getKey().equals(CAPITAL)) {
                InputData inputData = new InputData();
                inputData.setId(entry.getKey());
                inputData.setValue(new DecimalFormat("#0.00").
                    format(Double.valueOf(entry.getValue().getValue()) * partPrice).replace(",", "."));
                stepperData.replace(entry.getKey(), inputData);
            }
        }

        return stepperData;
    }
}
