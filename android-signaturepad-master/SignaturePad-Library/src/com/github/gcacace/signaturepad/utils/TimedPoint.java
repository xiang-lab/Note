package com.github.gcacace.signaturepad.utils;

public class TimedPoint {
  	  public final float x;
  	  public final float y;
  	  public final long timestamp;
  	  
  	  public TimedPoint(float x, float y) {
  		  this.x = x;
  		  this.y = y;
  		  this.timestamp = System.currentTimeMillis();
  	  }

  	  public float velocityFrom(TimedPoint start) {
		return distanceTo(start) / (this.timestamp - start.timestamp);
	}

	// 巨蠢的距离算法
  	  public float distanceTo(TimedPoint point) {
  		  return (float) Math.sqrt(Math.pow(point.x-this.x, 2) + Math.pow(point.y-this.y, 2));
  	  }
}