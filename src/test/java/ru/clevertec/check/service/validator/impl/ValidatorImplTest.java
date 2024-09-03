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

import static org.mockito.Mockito.*;

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
        TestObject testObject = new TestObject(collection, string, bigDecimal, i);

        List<String> messages = Collections.emptyList();
        validator.validate(testObject);


        verify(minExecutor).execute(testObject, messages);
        verify(patternExecutor).execute(testObject, messages);
        verify(notEmptyExecutor).execute(testObject, messages);
        verify(notNullExecutor).execute(testObject, messages);
        verify(validCollectionExecutor).execute(testObject, messages);
    }


    private record TestObject(
            @ValidCollection @NotEmpty List<String> collection,
            @Pattern String string,
            @NotNull BigDecimal balanceDebitCard,
            @Min int quantity) {

    }

}