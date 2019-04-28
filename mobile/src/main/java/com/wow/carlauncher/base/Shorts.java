package com.wow.carlauncher.base;

public class Shorts {
    private Shorts() {
    }

    /**
     * The number of bytes required to represent a primitive {@code short} value.
     *
     * <p><b>Java 8 users:</b> use {@link Short#BYTES} instead.
     */
    public static final int BYTES = Short.SIZE / Byte.SIZE;

    /**
     * The largest power of two that can be represented as a {@code short}.
     *
     * @since 10.0
     */
    public static final short MAX_POWER_OF_TWO = 1 << (Short.SIZE - 2);

    /**
     * Returns a hash code for {@code value}; equal to the result of invoking {@code ((Short)
     * value).hashCode()}.
     *
     * <p><b>Java 8 users:</b> use {@link Short#hashCode(short)} instead.
     *
     * @param value a primitive {@code short} value
     * @return a hash code for the value
     */
    public static int hashCode(short value) {
        return value;
    }

    /**
     * Returns the {@code short} nearest in value to {@code value}.
     *
     * @param value any {@code long} value
     * @return the same value cast to {@code short} if it is in the range of the {@code short} type,
     * {@link Short#MAX_VALUE} if it is too large, or {@link Short#MIN_VALUE} if it is too small
     */
    public static short saturatedCast(long value) {
        if (value > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        }
        if (value < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        }
        return (short) value;
    }

    /**
     * Compares the two specified {@code short} values. The sign of the value returned is the same as
     * that of {@code ((Short) a).compareTo(b)}.
     *
     * <p><b>Note for Java 7 and later:</b> this method should be treated as deprecated; use the
     * equivalent {@link Short#compare} method instead.
     *
     * @param a the first {@code short} to compare
     * @param b the second {@code short} to compare
     * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is
     * greater than {@code b}; or zero if they are equal
     */
    public static int compare(short a, short b) {
        return a - b; // safe due to restricted range
    }

    /**
     * Returns {@code true} if {@code target} is present as an element anywhere in {@code array}.
     *
     * @param array  an array of {@code short} values, possibly empty
     * @param target a primitive {@code short} value
     * @return {@code true} if {@code array[i] == target} for some value of {@code i}
     */
    public static boolean contains(short[] array, short target) {
        for (short value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the first appearance of the value {@code target} in {@code array}.
     *
     * @param array  an array of {@code short} values, possibly empty
     * @param target a primitive {@code short} value
     * @return the least index {@code i} for which {@code array[i] == target}, or {@code -1} if no
     * such index exists.
     */
    public static int indexOf(short[] array, short target) {
        return indexOf(array, target, 0, array.length);
    }

    // TODO(kevinb): consider making this public
    private static int indexOf(short[] array, short target, int start, int end) {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the start position of the first occurrence of the specified {@code target} within
     * {@code array}, or {@code -1} if there is no such occurrence.
     *
     * <p>More formally, returns the lowest index {@code i} such that {@code Arrays.copyOfRange(array,
     * i, i + target.length)} contains exactly the same elements as {@code target}.
     *
     * @param array  the array to search for the sequence {@code target}
     * @param target the array to search for as a sub-sequence of {@code array}
     */
    public static int indexOf(short[] array, short[] target) {
        if (target.length == 0) {
            return 0;
        }

        outer:
        for (int i = 0; i < array.length - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    /**
     * Returns the index of the last appearance of the value {@code target} in {@code array}.
     *
     * @param array  an array of {@code short} values, possibly empty
     * @param target a primitive {@code short} value
     * @return the greatest index {@code i} for which {@code array[i] == target}, or {@code -1} if no
     * such index exists.
     */
    public static int lastIndexOf(short[] array, short target) {
        return lastIndexOf(array, target, 0, array.length);
    }

    // TODO(kevinb): consider making this public
    private static int lastIndexOf(short[] array, short target, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the least value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code short} values
     * @return the value present in {@code array} that is less than or equal to every other value in
     * the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static short min(short... array) {
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    /**
     * Returns the greatest value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code short} values
     * @return the value present in {@code array} that is greater than or equal to every other value
     * in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static short max(short... array) {
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Returns the value nearest to {@code value} which is within the closed range {@code [min..max]}.
     *
     * <p>If {@code value} is within the range {@code [min..max]}, {@code value} is returned
     * unchanged. If {@code value} is less than {@code min}, {@code min} is returned, and if {@code
     * value} is greater than {@code max}, {@code max} is returned.
     *
     * @param value the {@code short} value to constrain
     * @param min   the lower bound (inclusive) of the range to constrain {@code value} to
     * @param max   the upper bound (inclusive) of the range to constrain {@code value} to
     * @throws IllegalArgumentException if {@code min > max}
     * @since 21.0
     */
    public static short constrainToRange(short value, short min, short max) {
        return value < min ? min : value < max ? value : max;
    }

    /**
     * Returns the values from each provided array combined into a single array. For example, {@code
     * concat(new short[] {a, b}, new short[] {}, new short[] {c}} returns the array {@code {a, b,
     * c}}.
     *
     * @param arrays zero or more {@code short} arrays
     * @return a single array containing all the values from the source arrays, in order
     */
    public static short[] concat(short[]... arrays) {
        int length = 0;
        for (short[] array : arrays) {
            length += array.length;
        }
        short[] result = new short[length];
        int pos = 0;
        for (short[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    /**
     * Returns a big-endian representation of {@code value} in a 2-element byte array; equivalent to
     * {@code ByteBuffer.allocate(2).putShort(value).array()}. For example, the input value {@code
     * (short) 0x1234} would yield the byte array {@code {0x12, 0x34}}.
     *
     * <p>If you need to convert and concatenate several values (possibly even of different types),
     * use a shared {@link java.nio.ByteBuffer} instance, or use {@link
     */
    public static byte[] toByteArray(short value) {
        return new byte[]{(byte) (value >> 8), (byte) value};
    }

    /**
     * Returns the {@code short} value whose big-endian representation is stored in the first 2 bytes
     * of {@code bytes}; equivalent to {@code ByteBuffer.wrap(bytes).getShort()}. For example, the
     * input byte array {@code {0x54, 0x32}} would yield the {@code short} value {@code 0x5432}.
     *
     * <p>Arguably, it's preferable to use {@link java.nio.ByteBuffer}; that library exposes much more
     * flexibility at little cost in readability.
     *
     * @throws IllegalArgumentException if {@code bytes} has fewer than 2 elements
     */
    public static short fromByteArray(byte[] bytes) {
        return fromBytes(bytes[0], bytes[1]);
    }

    /**
     * Returns the {@code short} value whose byte representation is the given 2 bytes, in big-endian
     * order; equivalent to {@code Shorts.fromByteArray(new byte[] {b1, b2})}.
     *
     * @since 7.0
     */
    public static short fromBytes(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }

}
