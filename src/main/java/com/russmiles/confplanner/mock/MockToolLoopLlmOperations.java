package com.russmiles.confplanner.mock;

import com.embabel.agent.api.common.Asyncer;
import com.embabel.agent.api.event.observation.AgentInstrumentation;
import com.embabel.agent.core.support.LlmInteraction;
import com.embabel.agent.spi.AutoLlmSelectionCriteriaResolver;
import com.embabel.agent.spi.ToolDecorator;
import com.embabel.agent.spi.loop.ToolLoopFactory;
import com.embabel.agent.spi.support.LlmDataBindingProperties;
import com.embabel.agent.spi.support.LlmOperationsPromptsProperties;
import com.embabel.agent.spi.support.OutputConverter;
import com.embabel.agent.spi.support.ToolLoopLlmOperations;
import com.embabel.agent.spi.validation.ValidationPromptGenerator;
import com.embabel.common.ai.model.ModelProvider;
import com.embabel.common.textio.template.TemplateRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;

/**
 * The tool-loop operations for the {@code mock} profile: it delegates message sending to whatever
 * {@link com.embabel.agent.spi.LlmService} is resolved (our {@link MockLlmService}, whose
 * {@code createMessageSender} returns canned JSON), and supplies the JSON&rarr;object converter.
 *
 * <p>Why subclass? {@code ToolLoopLlmOperations} already routes through the generic
 * {@code LlmService.createMessageSender} seam — exactly what we want — but its
 * {@code createOutputConverter} returns {@code null}: the concrete Spring-AI parser lives only in the
 * {@code ChatClientLlmOperations} subclass (which we cannot use, because it requires a real
 * {@code SpringAiLlmService}). So we plug the one missing piece: a Jackson converter that parses the
 * stub's canned text into the target record. Everything else — the structured-output request
 * wrapping, the tool loop, retries — is the framework's own.
 */
public class MockToolLoopLlmOperations extends ToolLoopLlmOperations {

    private final ObjectMapper objectMapper;

    public MockToolLoopLlmOperations(
            ModelProvider modelProvider,
            ToolDecorator toolDecorator,
            Validator validator,
            ValidationPromptGenerator validationPromptGenerator,
            LlmDataBindingProperties dataBindingProperties,
            AutoLlmSelectionCriteriaResolver autoLlmSelectionCriteriaResolver,
            LlmOperationsPromptsProperties promptsProperties,
            ObjectMapper objectMapper,
            AgentInstrumentation agentInstrumentation,
            Asyncer asyncer,
            ToolLoopFactory toolLoopFactory,
            TemplateRenderer templateRenderer) {
        super(modelProvider, toolDecorator, validator, validationPromptGenerator,
                dataBindingProperties, autoLlmSelectionCriteriaResolver, promptsProperties,
                objectMapper, agentInstrumentation, asyncer, toolLoopFactory, templateRenderer);
        this.objectMapper = objectMapper;
    }

    @Override
    protected <O> OutputConverter<O> createOutputConverter(Class<O> outputClass, LlmInteraction interaction) {
        return new JacksonOutputConverter<>(outputClass, objectMapper);
    }

    /** Parses the stub's canned JSON text into the requested record; empty format (the stub ignores it). */
    private record JacksonOutputConverter<O>(Class<O> outputClass, ObjectMapper objectMapper)
            implements OutputConverter<O> {

        @Override
        public O convert(String text) {
            try {
                return objectMapper.readValue(text, outputClass);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "mock model returned text that did not parse into " + outputClass.getSimpleName()
                                + ": " + text, e);
            }
        }

        @Override
        public String getFormat() {
            return "";
        }

        @Override
        public String getJsonSchema() {
            return "";
        }
    }
}
