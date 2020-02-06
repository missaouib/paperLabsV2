package com.apeiron.paperlabs.service.mapper.documents;

import com.apeiron.docflow.domain.InputData;
import com.apeiron.paperlabs.repository.CompanyRepository;
import com.apeiron.paperlabs.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Map;

@Service
public class StepperDataToEntityMapper {
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;

    public StepperDataToEntityMapper(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }


//    for (let i = 0; i < userChosenCompanies.length; i++) {
//        const companyAutoFillInputIdsByFieldName: any = this.legalDocument.companiesAutoFillInputIdsByFieldName[i];
//        const userChosenCompany: any = userChosenCompanies[i];
//        Object.keys(companyAutoFillInputIdsByFieldName).forEach(key => {
//            JhiAutoFillModalComponent.setHTMLElementValue(companyAutoFillInputIdsByFieldName[key], userChosenCompany[key]);
//        });
//    }

    public Object stepperDataToCompany (Object entity, Map<String, String> entityAutoFillInputIdsByFieldName, Map<String, InputData> stepperDataDTO) {
        Class<?> aClass = entity.getClass();
        Class aClassFieldType = null;
        Method method = null;
        String aClassFieldSetterMethodName;
        String aClassFieldName;
        String aClassFieldNameWithCapitalizedFirstLetter;
        Object aClassFieldValue;

        for(Map.Entry<String, String> entityAutoFillInputIdsByFieldNameEntry: entityAutoFillInputIdsByFieldName.entrySet()) {
            aClassFieldName = entityAutoFillInputIdsByFieldNameEntry.getKey();
            aClassFieldNameWithCapitalizedFirstLetter = aClassFieldName.substring(0, 1).toUpperCase() + aClassFieldName.substring(1);
            aClassFieldSetterMethodName = "set"+aClassFieldNameWithCapitalizedFirstLetter;
            try {
                aClassFieldType = aClass.getDeclaredField(aClassFieldName).getType();
                if(aClassFieldType == null) {
                    throw new RuntimeException(String.format("Field with name \"%s\" not found in class \"%s\"", aClassFieldName, aClass.getSimpleName()));
                }
                method = aClass.getMethod(aClassFieldSetterMethodName, aClassFieldType);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Setter method \"%s(%s param)\" for field \"%s\" not found in class \"%s\"",aClassFieldSetterMethodName, aClassFieldType.getSimpleName(), aClassFieldName, entity.getClass().getName()));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            try {
                String dataValue;
                if(stepperDataDTO.get(entityAutoFillInputIdsByFieldNameEntry.getValue()) != null) {
                    dataValue = stepperDataDTO.get(entityAutoFillInputIdsByFieldNameEntry.getValue()).getValue();
                }else {
                    dataValue = "";
                }
                aClassFieldValue = this.toObject(aClassFieldType, dataValue);
                method.invoke(entity, aClassFieldValue);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NullPointerException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return entity;
    }

    private Object toObject( Class aClassFieldType, String aClassFieldValue ) {
        String aClassFieldTypeClassName = aClassFieldType.getSimpleName();
        switch (aClassFieldTypeClassName) {
            case "String":
                return aClassFieldValue;
            case "Boolean":
                if(("").equals(aClassFieldValue)) {
                    return Boolean.FALSE;
                }
                return Boolean.parseBoolean( aClassFieldValue );
            case "boolean":
                if(("").equals(aClassFieldValue)) {
                    return false;
                }
                return Boolean.parseBoolean( aClassFieldValue );
            case "Byte":
            case "byte":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }
                return Byte.parseByte( aClassFieldValue );
            case "Short":
            case "short":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }
                return Short.parseShort( aClassFieldValue );
            case "Integer":
            case "int":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }
                return Integer.parseInt( aClassFieldValue );
            case "Long":
                if(("").equals(aClassFieldValue)) {
                    return 0L;
                }
                return Long.parseLong( aClassFieldValue );
            case "long":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }
                return Long.parseLong( aClassFieldValue );
            case "Float":
                if(("").equals(aClassFieldValue)) {
                    return 0F;
                }
                return Float.parseFloat( aClassFieldValue );
            case "float":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }else {
                    return Float.parseFloat( aClassFieldValue );
                }
            case "Double":
                if(("").equals(aClassFieldValue)) {
                    return 0D;
                }else {
                    return Double.parseDouble( aClassFieldValue );
                }
            case "double":
                if(("").equals(aClassFieldValue)) {
                    return 0;
                }else {
                    return Double.parseDouble( aClassFieldValue );
                }
            case "LocalDate":
                if(("").equals(aClassFieldValue)) {
                    return LocalDate.parse("2000-01-01");
                }else {
                    return  LocalDate.parse( aClassFieldValue );
                }
            default:
                throw new RuntimeException(String.format("Type %s is not supported.", aClassFieldTypeClassName));
        }
    }
}
