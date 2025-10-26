package kr.co.nextplayer.next.lib.common.validation;

import kr.co.nextplayer.next.lib.common.constants.ApiState;
import kr.co.nextplayer.next.lib.common.exception.CommonLogicException;
import kr.co.nextplayer.next.lib.common.operation.RedisOperation;
import kr.co.nextplayer.next.lib.common.util.JsonUtil;
import kr.co.nextplayer.next.lib.common.util.LocalUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ValidationBingingResult {

	@Resource
	private RedisOperation redisOperation;

	public List<String> getErrors(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return processErrorMsg(bindingResult);
		}
		return new ArrayList<>();
	}

	public List<String> processErrorMsg(BindingResult result) {
		List<String> errorList = new ArrayList<>();

		result.getFieldErrors().forEach(fieldError -> {
			// Using a TreeMap for alphabetical ordering of attribute names
			Object[] arguments = fieldError.getArguments();
			arguments[0] = fieldError.getField();
			String resultMessage = null;
			try {
				resultMessage = getMessageSource(fieldError.getDefaultMessage(), arguments);
			} catch (Exception e) {
				e.printStackTrace();
			}
			errorList.add(resultMessage);
		});

		return errorList;
	}

	private Map<String, String> processErrorMsgMap(BindingResult result) {
		List<FieldError> list = result.getFieldErrors();
		HashMap<String, String> errors = new HashMap<>();

		list.forEach(fieldError -> {
			Object[] arguments = fieldError.getArguments();
			arguments[0] = fieldError.getField();
			String resultMessage = null;
			try {
				resultMessage = getMessageSource(fieldError.getDefaultMessage(), arguments);
			} catch (Exception e) {
				e.printStackTrace();
			}
			errors.put(fieldError.getField(), resultMessage);
		});

		return errors;
	}

	public String getMessageSource(String messageKey, Object[] args) throws Exception {
		Object msgObject = redisOperation.getEntireHashFromKey(messageKey);
		Map<String, Object> map = JsonUtil.objectToMap(msgObject);
		String langCode = LocalUtil.getLang();
		//메세지중 {0...1}부분 파라미터로 전환
		return MessageFormat.format(String.valueOf(map.get(langCode)), args);
	}

	public void checkParamsCommon(BindingResult bindingResult) throws CommonLogicException {
		List<String> errors = getErrors(bindingResult);
		if (errors.size() > 0) {
			throw new CommonLogicException(ApiState.PARAMETER.getCode(), "msg1_common_paramErr", null, errors.toArray());
		}
	}
}
