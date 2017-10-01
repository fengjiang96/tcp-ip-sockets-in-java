public class BruteForceCoding {
	private static byte byteVal = 101;
	private static short shortVal = 1001;
	private static int intVal = 1000001;
	private static long longVal = 1000000000001L;

	private final static int BSIZE = Byte.SIZE;
	private final static int SSIZE = Short.SIZE;
	private final static int ISIZE = Integer.SIZE;
	private final static int LSIZE = Long.SIZE;

	private final static int BYTEMASK = 0xFF;

	public static String byteArrayToDecimalString(byte[] bArray) {
		StringBuilder rtn = new StringBuilder();
		for (byte b : bArray) {
			rtn.append(b & BYTEMASK).append(" ");
		}
		return rtn.toString();
	}

	public static int encodeIntBigEndian(byte[] dst, long val, int offset, int size) {
		for (int i = 0; i < size; i++) {
			dst[offset++] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
		}
		return offset;
	}

	public static long decodeIntBigEndian(byte[] val, int offset, int size) {
		long rtn = 0;
		for (int i = 0; i < size; i++) {
			rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTEMASK);
		}
		return rtn;
	}

	public static void main(String[] args) {
		byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];

		int offset = encodeIntBigEndian(message, byteVal, 0, BSIZE);
		offset = encodeIntBigEndian(message, shortVal, offset, SSIZE);
		offset = encodeIntBigEndian(message, intVal, offset, ISIZE);
		encodeIntBigEndian(message, longVal, offset, LSIZE);
		System.out.println("Encode message: " + byteArrayToDecimalString(message));

		long value = decodeIntBigEndian(message, BSIZE, SSIZE);
		System.out.println("Decoded short = " + value);
		value = decodeIntBigEndian(message, BSIZE + SSIZE + ISIZE, LSIZE);
		System.out.println("Decoded long = " + value);

		offset = 4;
		value = decodeIntBigEndian(message, offset, BSIZE);
		System.out.println("Decoded value (offset " + offset + ", size " + BSIZE + ") = " +value);
		byte bVal = (byte) decodeIntBigEndian(message, offset, BSIZE);
		System.out.println("Same value as byte = " + bVal);
	}
}