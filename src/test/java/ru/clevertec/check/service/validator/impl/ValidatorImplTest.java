package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.service.validator.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidatorImplTest {

    @Mock
    private MinExecutor minExecutor;
    @Mock
    private PatternExecutor patternExecutor;
    @Mock
    private NotEmptyExecutor notEmptyExecutor;
    @Mock
    private NotNullExecutor notNullExecutor;
    @Mock
    private ValidCollectionExecutor validCollectionExecutor;

    private Validator validator;

    @BeforeEach
    public void init() {
        validator = new ValidatorImpl(minExecutor,
                patternExecutor,
                notEmptyExecutor,
                notNullExecutor,
                validCollectionExecutor);
    }

    @Test
    public void validate() {
        List<String> collection = Collections.emptyList();
        String string = "test";
        BigDecimal bigDecimal = BigDecimal.ZERO;
        int i = 0;
        ExpectedObject expectedObject = new ExpectedObject(collection, string, bigDecimal, i);

        List<String> messages = Collections.emptyList();
        validator.validate(expectedObject);


        verify(minExecutor).execute(expectedObject, messages);
        verify(patternExecutor).execute(expectedObject, messages);
        verify(notEmptyExecutor).execute(expectedObject, messages);
        verify(notNullExecutor).execute(expectedObject, messages);
        verify(validCollectionExecutor).execute(expectedObject, messages);
    }


    private record ExpectedObject(
            @ValidCollection @NotEmpty List<String> collection,
            @Pattern String string,
            @NotNull BigDecimal balanceDebitCard,
            @Min int quantity) {

    }

}