package com.mzone.main.events.validator;

import com.mzone.main.events.event.EventCategory;
import org.apache.logging.log4j.util.Strings;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * validate integer code of {@link com.mzone.main.events.event.EventCategory}
 */
@Documented
@Constraint(validatedBy = EventCategoryConstraint.EventCategoryValidator.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventCategoryConstraint {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class EventCategoryValidator implements ConstraintValidator<EventCategoryConstraint, Integer> {

        private String message;

        @Override
        public void initialize(EventCategoryConstraint constraintAnnotation) {
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            boolean isValid = true;
            String generatedMessage = "";

            if (value == null) {
                generatedMessage = "Can't be null";
                isValid = false;
            } else if (value < 0 || value >= EventCategory.VALUES.length) {
                generatedMessage = "Can't be less than 0 and more than " + (EventCategory.VALUES.length - 1);
                isValid = false;
            }

            if (Strings.isBlank(message)) {
                message = generatedMessage;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return isValid;
        }
    }
}
