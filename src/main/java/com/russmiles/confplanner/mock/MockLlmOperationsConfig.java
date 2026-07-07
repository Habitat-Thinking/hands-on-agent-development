package com.russmiles.confplanner.mock;

import com.embabel.agent.api.common.Asyncer;
import com.embabel.agent.api.event.observation.AgentInstrumentation;
import com.embabel.agent.core.internal.LlmOperations;
import com.embabel.agent.spi.AutoLlmSelectionCriteriaResolver;
import com.embabel.agent.spi.ToolDecorator;
import com.embabel.agent.spi.loop.ToolLoopFactory;
import com.embabel.agent.spi.support.LlmDataBindingProperties;
import com.embabel.agent.spi.support.LlmOperationsPromptsProperties;
import com.embabel.agent.spi.validation.ValidationPromptGenerator;
import com.embabel.common.ai.model.ModelProvider;
import com.embabel.common.textio.template.TemplateRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;

/**
 * Routes the LLM-operations layer through the pure-JVM {@link LlmService} seam under the {@code mock}
 * profile, so {@link MockLlmService#createMessageSender} is actually the code that runs.
 *
 * <p>Why this is needed: the default, component-scanned operations bean is
 * {@code ChatClientLlmOperations}, which overrides {@code createMessageSender} to <em>require</em> a
 * concrete {@code SpringAiLlmService} (it builds a Spring AI {@code ChatClient} around a real
 * provider {@code ChatModel}). A hand-written stub {@code LlmService} is rejected with
 * "requires SpringAiLlm". Its superclass {@code ToolLoopLlmOperations}, however, uses the generic
 * seam: it calls {@code llm.createMessageSender(options)} and wraps the result in a
 * {@code StructuredOutputLlmMessageSender} that appends the JSON schema and parses the returned text
 * into the target type. So under {@code mock} we register a {@code @Primary} plain
 * {@code ToolLoopLlmOperations}, and our canned-JSON sender becomes the model.
 *
 * <p>Everything here is {@code @Profile("mock")} only. The keyless {@code ./mvnw verify} (mock not
 * active) keeps the stock {@code ChatClientLlmOperations} and is entirely unaffected.
 */
@Configuration
@Profile("mock")
public class MockLlmOperationsConfig {

    /**
     * The stock dependencies are all ordinary platform beans; we assemble them into the base
     * tool-loop operations (the one that honours {@code LlmService.createMessageSender}) and mark it
     * {@code @Primary} so it wins over the component-scanned {@code ChatClientLlmOperations}.
     */
    @Bean
    @Primary
    public LlmOperations mockLlmOperations(
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
        return new MockToolLoopLlmOperations(
                modelProvider,
                toolDecorator,
                validator,
                validationPromptGenerator,
                dataBindingProperties,
                autoLlmSelectionCriteriaResolver,
                promptsProperties,
                objectMapper,
                agentInstrumentation,
                asyncer,
                toolLoopFactory,
                templateRenderer);
    }
}
