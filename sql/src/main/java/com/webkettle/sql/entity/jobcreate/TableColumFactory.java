package com.webkettle.sql.entity.jobcreate;

/**
 * 效率适得其反的表列工厂
 * @author gsk
 */
//public class TableColumFactory {
//
//    /**
//     * 列池化
//     */
//    private static final LinkedList<TableColum> createdColums = new LinkedList<>();
//
//    private static final Object block = new Object();
//
//    private static boolean initing = false;
//
//    /**
//     * 获得一个TableColum的实例
//     * @return
//     *          TableColum
//     */
//    public static TableColum getInstance(){
//        TableColum tableColum = null;
//        synchronized (block){
//            if (createdColums.size() > 0){
//                tableColum = createdColums.removeFirst();
//            }else{
//                tableColum = new TableColum();
//                if (!initing){
//                    initing = true;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            initTableColums(1000);
//                        }
//                    }).start();
//                }
//
//            }
//            return tableColum;
//        }
//    }
//
//    private static void initTableColums(int size){
//
//
//        for(int i = createdColums.size(); i < size; i++){
//            synchronized (block){
//                createdColums.add(new TableColum());
//            }
//        }
//        initing = false;
//    }
//
//
//    public static void main(String[] args){
//        long newStart = System.currentTimeMillis();
//        for(int i = 0; i < 100000000; i++){
//            new TableColum();
//        }
//        System.out.println("newTime:" + (System.currentTimeMillis() - newStart));
//
//        long newStart2 = System.currentTimeMillis();
//        for(int i = 0; i < 100000000; i++){
//            TableColumFactory.getInstance();
//        }
//        System.out.println("getInstanceTime:" + (System.currentTimeMillis() - newStart2));
//
//        // 尴尬了...
//    }
//
//
//}
