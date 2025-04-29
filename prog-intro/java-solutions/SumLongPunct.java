public class SumLongPunct {
    public static void main(String[] args) {
        long sum = 0;
        for (String arg : args) {
            String stroka = arg + " ";
            int index = 0;
            int index2 = 0;
            while (index != stroka.length()) {
                StringBuilder string = new StringBuilder();
                if (isDigit(stroka.charAt(index))) {
                    while (isDigit(stroka.charAt(index))) {
                        string.append(stroka.charAt(index));
                        index++;
                    }
                    sum += Long.parseLong(string.toString());
                }
                index++;
            }
        }
        System.out.println(sum);
    }

    private static boolean isDigit(char s) {
        return !Character.isWhitespace(s) && Character.getType(s) != Character.START_PUNCTUATION && Character.getType(s) != Character.END_PUNCTUATION;
    }
}