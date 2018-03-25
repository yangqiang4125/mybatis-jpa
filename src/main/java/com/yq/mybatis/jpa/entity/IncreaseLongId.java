package com.yq.mybatis.jpa.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yangqiang-mac on 2018/3/25.
 */
public class IncreaseLongId implements IdGenerator<Long> {
   private AtomicLong id;

   public IncreaseLongId() {
      id = new AtomicLong();
   }

   public IncreaseLongId(long initialId) {
      id = new AtomicLong(initialId);
   }

   @Override
   public Long next() {
      return id.incrementAndGet();
   }
}
