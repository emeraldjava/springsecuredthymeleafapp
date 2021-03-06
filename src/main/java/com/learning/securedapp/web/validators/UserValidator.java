package com.learning.securedapp.web.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.learning.securedapp.domain.User;
import com.learning.securedapp.web.services.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * <p>UserValidator class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@Component
public class UserValidator implements Validator {

	@Autowired
	protected SecurityService securityService;

	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		log.debug("Validating {}", target);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "message.password", "Password is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "message.username", "UserName is required.");
		User user = (User) target;
		validateEmail(errors, user);
	}

	private void validateEmail(Errors errors, User user) {
		String email = user.getEmail();
		if (StringUtils.isNotBlank(email)) {
			User userByEmail = securityService.findUserByEmail(email);
			if (userByEmail != null) {
				errors.rejectValue("email", "error.exists", new Object[] { email },
						"Email " + email + " already in use");
			}
		} else {
			errors.rejectValue("email", "error.emailnull", new Object[] { email }, "Email cannot be null");
		}

	}

}
