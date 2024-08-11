package Sites;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;
/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Class        Validation
 * File         Validation.java
 * Description  Validates entered values
 * @author      <i>Kirill Grichanichenko</i>
 * Date         10/26/2023
 * @see         java.util.regex.Matcher
 * @see         java.util.regex.Pattern
 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
public class Validation 
{   
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isLettersOnly()
     * Description  Validates that double value is entered
     * @return      Boolean
     * @param       myString String
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isLettersOnly(String myString)
    {
        // Check if the String is empty
        if(myString.isEmpty())
            return false;
        // Make a char array for the string and check each character
        char[] charArray = myString.toCharArray();
        for(int i = 0; i < charArray.length; i++)
        {
            if(!Character.isLetter(charArray[i]) && !Character.isSpaceChar(charArray[i]))
                return false;
        }
        // It is letters and space only!
        return true;
    }
    
    public static boolean isNumeric(String myString)
    {
        // Check if the string contains only numbers or a decimal or negative sign
        return myString.matches("-?\\d+(\\.\\d+)?");
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isDouble()
     * Description  Validates that double value is entered
     * @return      Boolean
     * @param       fieldValue String
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isDouble(String fieldValue)
    {
        Pattern pat = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher mat = pat.matcher(fieldValue);
        return mat.matches();
    }
   
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isDouble()
     * Description  Overloaded, validates that double value is entered within
     *              the required range
     * @param       fieldValue String, input
     * @param       lower double, lower bound
     * @param       upper double, upper bound
     * @return      Boolean
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isDouble(String fieldValue, double lower, double upper)
    {
        boolean result = true;
        Pattern pat = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher mat = pat.matcher(fieldValue);
        if(mat.matches())
        {
            try
            {
                //check range
                double num = Double.parseDouble(fieldValue);
                if(num < lower || num > upper)
                    result = false;
            }
            catch(NumberFormatException ex)
            {
                //something went wrong
                result = false;
            }
        }
        else
        {
            result = false;
        }
        return result;
        //return mat.matches();
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isInteger()
     * Description  Validates that entered value is an integer
     * @return      Boolean
     * @param       fieldValue String, input
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isInteger(String fieldValue)
    {
        Pattern pat = Pattern.compile("\\d+");
        Matcher mat = pat.matcher(fieldValue);
        return mat.matches();   
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isInteger()
     * Description  Overloaded, validates that entered value is an integer
     *              within the required range
     * @return      Boolean
     * @param       fieldValue String, input
     * @param       lower int, lower bound
     * @param       upper int, upper bound
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isInteger(String fieldValue, int lower, int upper)
    {
        boolean result = true;
        Pattern pat = Pattern.compile("\\d+");
        Matcher mat = pat.matcher(fieldValue);
        if(mat.matches())
        {
            try
            {
                //check range
                int num = Integer.parseInt(fieldValue);
                if(num < lower || num > upper)
                    result = false;
            }
            catch(NumberFormatException ex)
            {
                //something went wrong
                result = false;
            }
        }
        else
        {
            result = false;
        }
        return result;           
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isEmpty()
     * Description  Validates that JTextField is not empty
     * @return      Boolean
     * @param       fieldValue: JTextField, input
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
     * Date         10/26/2023
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isEmpty(JTextField fieldValue)
    {
        String input = fieldValue.getText();
        if(input.length() <= 0 || input.equals(""))
            return true;
        else
            return false;
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isValidName()
     * Description  Validates that JTextField is a valid name consisting of 
     *              only letters and spaces with minimum 2 and maximum 40
     *              letters.
     * Date         10/26/2023
     * @return      Boolean
     * @param       input: JTextField, input
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isValidName(String input)
    {
        final short MAX_LENGTH = 40;
        final short MIN_LENGTH = 2;
        boolean result = true;
        if(input.equals("") || input.length() <= MIN_LENGTH || 
                input.length() > MAX_LENGTH || input.equals(null))
            return false;
        //Lettera and spaces in unicode only
        //String regx = "\\^[a-zA-Z '.-]*$"; //"\\^[a-zA-Z '.-]{3,30}$";      
        //String regx = "\\[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
        String regx =  "^[\\p{L} _.'-]+$";     //"^\\p{Lu}\\p{L}*(?:[\\s-]\\p{Lu}\\p{L}*)*$";
        Pattern pat = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(input);    
        result = mat.matches();
        return result;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isValidName()--overloaded
     * Description  Validates that JTextField is a valid name consisting of 
     *              only letters and spaces with minimum and maximum as provide
     *              parameters.
     * Date         10/26/2023
     * @return      true or false Boolean
     * @param       input JTextField
     * @param       lower int
     * @param       upper int
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isValidName(String input, int lower, int upper)
    {
        boolean result = true;
        if(input.equals("") || input.length() <= lower || 
                input.length() > upper || input.equals(null))
            return false;
        //Lettera and spaces in unicode only
        //String regx = "\\^[a-zA-Z '.-]*$"; //"\\^[a-zA-Z '.-]{3,30}$";      
        //String regx = "\\[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
        String regx =  "^[\\p{L} _.'-]+$";     //"^\\p{Lu}\\p{L}*(?:[\\s-]\\p{Lu}\\p{L}*)*$";
        Pattern pat = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(input);    
        result = mat.matches();
        return result;
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isValidPhone()
     * Description  Validates that JTextField is a valid North American phone
     *              number with the following phone formats:
     *              1234567890: true
     *              123-456-7890: true
     *              123.456.7890: true
     *              123 456 7890: true
     *              (123) 456 7890: true
     *              12345678: false
     *              12-12-111: false
     * Date         10/26/2023
     * @return      Boolean
     * @param       fieldValue JTextField
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isValidPhone(String fieldValue)
    {
        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(fieldValue);
        return mat.matches();
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isValidEmail()
     * Description  Validates that JTextField is a valid email with the 
     *              following email formats:
     *              user@domain.com true
     *              user@domain.co.in true
     *              user.name@domain.com true
     *              user?name@domain.co.in true
     *              user'name@domain.co.in true
     * Date         10/26/2023
     * @return      Boolean
     * @param       fieldValue JTextField
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isValidEmail(String fieldValue)
    {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(fieldValue);
        return mat.matches();
    }
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * Method       isValidURL()
     * Description  Validates that fieldValue is a URL address
     * Date         10/26/2023
     * @return      true or false Boolean
     * @param       fieldValue JTextField
     * @author      <i>Kirill Grichanichenko</i>
     * @see         java.util.regex.Matcher
     * @see         java.util.regex.Pattern
     * @see         javax.swing.JTextField
    *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static boolean isValidURL(String fieldValue)
    {
        //String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        //String regex =  "^(https?:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
        //String regex =  "(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$";
        //String regex =  "^(https?:\\\\/\\\\/)?(www\\.)?([\\\\w]+\\\\.)+[‌​\\\\w]{2,63}\\\\/?$";
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(fieldValue);
        return mat.matches();
    }
    
    public static boolean isGreaterThan(String fieldValue1, String fieldValue2) {
    try {
            int value1 = Integer.parseInt(fieldValue1);
            int value2 = Integer.parseInt(fieldValue2);

            return value1 > value2;
        } catch (NumberFormatException e) {
            // Handle the case where either fieldValue1 or fieldValue2 cannot be parsed as an integer
            return false; // or throw an exception, show an error message, etc.
        }
    }
}