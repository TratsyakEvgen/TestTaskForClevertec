package ru.clevertec.check.service.validator.impl;


import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.service.validator.annotation.*;

import java.util.ArrayList;
import java.util.List;


@NoSpringComponent
public class ValidatorImpl implements Validator {
    @Inject
    private AnnotationExecutor<Min> minExecutor;
    @Inject
    private AnnotationExecutor<Pattern> patternExecutor;
    @Inject
    private AnnotationExecutor<NotEmpty> notEmptyExecutor;
    @Inject
    private AnnotationExecutor<NotNull> notNullExecutor;
    @Inject
    private AnnotationExecutor<ValidCollection> validCollectionExecutor;


    public ValidatorImpl() {
    }

    public ValidatorImpl(MinExecutor minExecutor,
                         PatternExecutor patternExecutor,
                         NotEmptyExecutor notEmptyExecutor,
                         NotNullExecutor notNullExecutor,
                         ValidCollectionExecutor validCollectionExecutor) {
        this.minExecutor = minExecutor;
        this.patternExecutor = patternExecutor;
        this.notEmptyExecutor = notEmptyExecutor;
        this.notNullExecutor = notNullExecutor;
        this.validCollectionExecutor = validCollectionExecutor;
    }

    @Override
    public <T> List<String> validate(T obj) {

        List<AnnotationExecutor<?>> executors =
                List.of(minExecutor, patternExecutor, notEmptyExecutor, notNullExecutor, validCollectionExecutor);

        List<String> errors = new ArrayList<>();

        executors.forEach(executorMap -> executorMap.execute(obj, errors));
        return errors;
    }

    public AnnotationExecutor<ValidCollection> getValidCollectionExecutor() {
        return validCollectionExecutor;
    }

    public void setValidCollectionExecutor(AnnotationExecutor<ValidCollection> validCollectionExecutor) {
        this.validCollectionExecutor = validCollectionExecutor;
    }
}
