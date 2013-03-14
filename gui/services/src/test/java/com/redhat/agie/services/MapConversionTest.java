/**
 * 
 */
package com.redhat.agie.services;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author bashburn
 *
 */
public class MapConversionTest {
  /**
   * Location Notes:
   * Origin: 84, 550
   * 
   * 34L - x:123, y:395 - relative: x:7,   y:1
   * 34C - x:190, y:433 - relative: x:5.5, y:6
   * 34R - x:219, y:530 - relative: x:2,   y:8
   * 
   * 16R - x:123, y:70  - relative: x:7,   y:24.8
   * 16C - x:190, y:70  - relative: x:5.5, y:24.8
   * 16L - x:219, y:70  - relative: x:2,   y:24.8
   * 
   * f(x) = x'
   * g(y) = y'
   * 
   * f(123) = 7
   * f(190) = 5.5
   * f(219) = 2
   * 
   * g(70)  = 24.8
   * g(395) = 1
   * g(433) = 6
   * g(530) = 8
   */
  float[][] xVals = new float[][]{
      new float[] {84,  0},
      new float[] {219, 7},
      new float[] {190, 5.5f},
      new float[] {123, 2}
  };
  float[][] yVals = new float[][]{
      new float[] {550, 0},
      new float[] {530, 1},
      new float[] {433, 6},
      new float[] {395, 8},
      new float[] {70, 25.2f}
  };
  float diff = 3f;
  
  @Test
  public void checkFunction() {
    for(float[] testVals : xVals) {
      assertEquals(testVals[0], convertX(testVals[1]), diff);
    }
    for(float[] testVals : yVals) {
      assertEquals(testVals[0], convertY(testVals[1]), diff);
    }
  }
  
  /**
   * Origin is at 84
   * Grid width = 19
   * 
   * f(x) = (x - 84) / 19
   * @param x
   * @return
   */
  public float convertX(float x) {
    return x * 19 + 84f;
  }
  /**
   * Origin is at 550
   * 
   * @param y
   * @return
   */
  public float convertY(float y) {
    return 550 - (y * 19);
  }
}
