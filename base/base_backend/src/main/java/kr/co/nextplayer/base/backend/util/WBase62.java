package kr.co.nextplayer.base.backend.util;

import java.math.BigInteger;

public class WBase62 {

	// public static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	public static final String DIGITS = "EoqswITY2bkzMRUW034ilnptvxNO57cfayAj96gHrFKVXZ8C1PQJLdmhBGSDue";
	public static final BigInteger BASE = BigInteger.valueOf(62);
	public static final String REGEXP = "^[0-9A-Za-z]+$";

	/**
	 * Encodes a number using Base62 encoding.
	 *
	 * @param number
	 *            a positive integer
	 * @return a Base62 string
	 * @throws IllegalArgumentException
	 *             if <code>number</code> is a negative integer
	 */
	public static String encode(BigInteger number) {
		if (number.compareTo(BigInteger.ZERO) == -1) { // number < 0
			throw new IllegalArgumentException("number must not be negative");
		}
		StringBuilder result = new StringBuilder();
		while (number.compareTo(BigInteger.ZERO) == 1) { // number > 0
			BigInteger[] divmod = number.divideAndRemainder(BASE);
			number = divmod[0];
			int digit = divmod[1].intValue();
			result.insert(0, DIGITS.charAt(digit));
		}
		return (result.length() == 0) ? DIGITS.substring(0, 1) : result
				.toString();
	}

	public static String encode(long number) {
		return encode(BigInteger.valueOf(number));
	}

	/**
	 * Decodes a string using Base62 encoding.
	 *
	 * @param string
	 *            a Base62 string
	 * @return a positive integer
	 * @throws IllegalArgumentException
	 *             if <code>string</code> is empty
	 */
	public static BigInteger decode(final String string) {
		if (string.length() == 0) {
			throw new IllegalArgumentException("string must not be empty");
		}
		BigInteger result = BigInteger.ZERO;
		int digits = string.length();
		for (int index = 0; index < digits; index++) {
			int digit = DIGITS.indexOf(string.charAt(digits - index - 1));
			result = result.add(BigInteger.valueOf(digit).multiply(
					BASE.pow(index)));
		}
		return result;
	}

	// Examples
	public static void main(String[] args) throws InterruptedException {
		//String szNumber = "16880715010308923450160909102030";
		//String szNumber = "16090910203001688071501030892345";
		//String[] szNumber = {"803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101","803101",};
		String szNumber = "157711220313242114";
		/*
		int count = 100;
		for (int i = 1; i <= count; i++) {
			String szNumber = "8031";
//			String szNumber = "";
			int length = (int)(Math.log10(i)+1);
			//System.out.println("-- length : "+ length);
			if(length == 1){
				szNumber = szNumber+"0"+i;
			}else{
				szNumber = szNumber+i;
			}
		}
		 */
	//System.out.println("-- szNum : "+ szNum);
	BigInteger number = new BigInteger( szNumber );
	
	String szEncoded = WBase62.encode(number);
	System.out.println(szNumber + " encoded to Base62: " + szEncoded);
	
	String szDecoded = WBase62.decode(szEncoded).toString();
	System.out.println(szEncoded + " decoded to Base62: " + szDecoded);
		

	}

}
