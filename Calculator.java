/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */

import static java.lang.Math.*;

/**

 @author EMAM
 */
public class Calculator {


          public static final double calculate(String str) {
                    str = str.replaceAll("[⅓]" , "(1./3)").replaceAll("⅔" , "(2./3)")
                            .replaceAll("⅕" , "(1./5)").replaceAll("⅖" , "(2./5)")
                            .replaceAll("⅗" , "(3./5)").replaceAll("⅘" , "(4./5)")
                            .replaceAll("⅙" , "(1./6)").replaceAll("⅚" , "(5./6)")
                            .replaceAll("⅐" , "(1./7)").replaceAll("⅛" , "(1./8)")
                            .replaceAll("⅜" , "(3./8)").replaceAll("⅝" , "(5./8)")
                            .replaceAll("⅞" , "(7./8)").replaceAll("⅑" , "(1./9)")
                            .replaceAll("⅒" , "(1./10)").replaceAll("" , "");
                    return calculate(str , false);
          }



          public static final double calculate(String str , boolean isDeg) {
                    return new Object() {
                              int pos = -1, ch;



                              void nextChar() {
                                        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                              }



                              boolean eat(int charToEat) {
                                        while ( ch == ' ' ) {
                                                  nextChar();
                                        }
                                        if ( ch == charToEat ) {
                                                  nextChar();
                                                  return true;
                                        }
                                        return false;
                              }



                              double parse() {
                                        nextChar();
                                        double x = parseExpression();
                                        if ( pos < str.length() ) {
                                                  throw new RuntimeException("Unexpected: " + (char) ch);
                                        }
                                        return x;
                              }



                              double parseExpression() {
                                        double x = parseTerm();
                                        for ( ;; ) {
                                                  if ( eat('+') ) {
                                                            x += parseTerm();
                                                  } else if ( eat('-') ) {
                                                            x -= parseTerm();
                                                  } else {
                                                            return x;
                                                  }
                                        }
                              }



                              double parseTerm() {
                                        double x = parseFactor();
                                        for ( ;; ) {
                                                  if ( eat('×') || eat('x') || eat('*') ) {
                                                            x *= parseFactor();
                                                  } else if ( eat('÷') || eat('/') || eat(':') ) {
                                                            x /= parseFactor();
                                                  } else if ( eat('%') ) {
                                                            x %= parseFactor();
                                                  } else {
                                                            return x;
                                                  }
                                        }
                              }



                              double parseFactor() {
                                        if ( eat('+') ) {
                                                  return parseFactor();
                                        }
                                        if ( eat('-') ) {
                                                  return -parseFactor();
                                        }
                                        double x;
                                        int startPos = this.pos;
                                        if ( eat('(') ) {
                                                  x = parseExpression();
                                                  eat(')');
                                        } else if ( eat('|') ) {
                                                  x = abs(parseExpression());
                                                  eat('|');
                                        } else if ( eat('e') ) {
                                                  x = E;
                                        } else if ( eat('π') || (eat('P') && eat('I')) ) {
                                                  x = PI;
                                        } else if ( (ch >= '0' && ch <= '9') || ch == '.' ) {
                                                  while ( (ch >= '0' && ch <= '9') || ch == '.' ) {
                                                            nextChar();
                                                  }
                                                  x = Double.parseDouble(str.substring(startPos , this.pos));
                                        } else if ( ch >= 'a' && ch <= 'z' || ch == '√' ) {
                                                  while ( ch >= 'a' && ch <= 'z' || ch == '√' || ch == '~' ) {
                                                            nextChar();
                                                  }
                                                  String func = str.substring(startPos , this.pos).toLowerCase();
                                                  x = parseFactor();
                                                  switch ( func ) {
                                                            case "√":
                                                            case "sqrt":
                                                                      x = sqrt(x);
                                                                      break;
                                                            case "∛":
                                                            case "cbrt":
                                                                      x = cbrt(x);
                                                                      break;
                                                            case "∜":
                                                                      x = pow(x , 1. / 4);
                                                                      break;
                                                            case "sin":
                                                                      x = sin(x , isDeg);
                                                                      break;
                                                            case "sinh":
                                                                      x = sinh(x , isDeg);
                                                                      break;
                                                            case "asin":
                                                                      x = asin(x , isDeg);
                                                                      break;
                                                            case "cos":
                                                                      x = cos(x , isDeg);
                                                                      break;
                                                            case "cosh":
                                                                      x = cosh(x , isDeg);
                                                                      break;
                                                            case "acos":
                                                                      x = acos(x , isDeg);
                                                                      break;
                                                            case "tan":
                                                                      x = tan(x , isDeg);
                                                                      break;
                                                            case "tanh":
                                                                      x = tanh(x , isDeg);
                                                                      break;
                                                            case "atan":
                                                                      x = atan(x , isDeg);
                                                                      break;
                                                            case "abs":
                                                                      x = abs(x);
                                                                      break;
                                                            case "log":
                                                            case "㏒":
                                                                      x = log(2 , x);
                                                                      break;
                                                            case "ln":
                                                            case "㏑":
                                                            case "log~e":
                                                                      x = ln(x);
                                                                      break;

                                                            default:
                                                                      throw new RuntimeException("Unknown function: " + func);
                                                  }
                                        } else {
                                                  throw new RuntimeException("Unexpected: " + (char) ch);
                                        }
                                        if ( eat('^') ) {
                                                  x = pow(x , parseFactor());
                                                  ////
                                        } else if ( eat('!') ) {
                                                  x = factorial((int) x);
                                        } else if ( eat('P') ) {
                                                  x = permutation((int) x , (int) parseFactor());
                                        } else if ( eat('C') ) {
                                                  x = combination((int) x , (int) parseFactor());
                                                  ////
                                        } else if ( eat('²') ) {
                                                  x = pow(x , 2);
                                        } else if ( eat('³') ) {
                                                  x = pow(x , 3);
                                        }
                                        return x;
                              }
                    }.parse();
          }



          public static final long factorial(int number) {
                    long fac = 1;
                    for ( int i = 1 ; i <= number ; i++ ) {
                              fac *= i;
                    }
                    return fac;
          }



          /**
           a P b = fac(a) / fac(b)
           */
          public static final long permutation(int a , int b) {
                    if ( b > a ) {
                              throw new ArithmeticException(a + " < " + b);
                    }
                    long per = 1;
                    for ( int i = a - b + 1 ; i <= a ; i++ ) {
                              per *= i;
                    }
                    return per;
          }



          public static final double combination(int a , int b) {
                    return permutation(a , b) / (double) factorial(b);
          }



          public static final double tan(double angle , boolean isDeg) {
                    return StrictMath.tan(isDeg ? toRadians(angle) : angle);
          }



          public static final double atan(double angle , boolean isDeg) {
                    return StrictMath.atan(isDeg ? toRadians(angle) : angle);
          }



          public static final double tanh(double angle , boolean isDeg) {
                    return Math.tanh(isDeg ? toRadians(angle) : angle);
          }



          public static final double cos(double angle , boolean isDeg) {
                    return StrictMath.cos(isDeg ? toRadians(angle) : angle);
          }



          public static final double acos(double angle , boolean isDeg) {
                    return StrictMath.acos(isDeg ? toRadians(angle) : angle);
          }



          public static final double cosh(double angle , boolean isDeg) {
                    return StrictMath.cosh(isDeg ? toRadians(angle) : angle);
          }



          public static final double sin(double angle , boolean isDeg) {
                    return StrictMath.sin(isDeg ? toRadians(angle) : angle);
          }



          public static final double asin(double angle , boolean isDeg) {
                    return StrictMath.asin(isDeg ? toRadians(angle) : angle);
          }



          public static final double sinh(double angle , boolean isDeg) {
                    return StrictMath.sinh(isDeg ? toRadians(angle) : angle);
          }



          /**

           @param num

           @return
           */
          public static final double ln(double num) {
                    return log(E , num);
          }



          /**

           @param base
           @param num

           @return
           */
          public static final double log(double base , double num) {
                    double first = 0;
                    double number = num;
                    while ( num > base ) {
                              num /= base;
                              first++;
                    }
                    if ( num == base ) {
                              return first + 1;
                    }
                    double power, last = first + 1, midd = (first + last) / 2;
                    do {
                              power = pow(base , midd);
                              if ( power > number && last != midd ) {
                                        last = midd;
                              } else if ( power < number && first != midd ) {
                                        first = midd;
                              } else {
                                        return midd;
                              }
                              midd = (first + last) / 2;
                    } while ( power != number );
                    return midd;
          }

}
