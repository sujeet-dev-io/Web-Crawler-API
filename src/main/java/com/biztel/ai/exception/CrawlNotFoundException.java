package com.biztel.ai.exception;

public class CrawlNotFoundException extends RuntimeException {
   public CrawlNotFoundException(String msg){
       super(msg);
   }
}
