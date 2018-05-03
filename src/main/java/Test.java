

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.xpay.pay.util.CommonUtils;

public class Test {
	public static String obj1 = "obj1";
    public static String obj2 = "obj2";

	public static void main(String args[]){
		List<Long> blackList = new ArrayList<Long>();
		blackList.add(3l);
		List<Long> list = new ArrayList<Long>();
		for(int i=0;i<5;i++){
		
			list.add(i+0l);
		}
		List<StoreChannelInfo> dbList = new ArrayList<StoreChannelInfo>();
		for(int i=0;i<5;i++){
			StoreChannelInfo item = new StoreChannelInfo();
			item.setId(i+1);
			item.setCnt(5*(5-i));
			
			dbList.add(item);
		}
		
	
		/*List<StoreChannelInfo> filterMap = null;
		filterMap = dbList.stream().filter(x->CommonUtils.in(list,x.getId()) && !CommonUtils.in(blackList,x.getId())
				).sorted(Comparator.comparing(StoreChannelInfo::getCnt)).collect(Collectors.toList());
		for(int i=0;i<filterMap.size();i++){
			System.out.println(filterMap.get(i).getId()+","+filterMap.get(i).getCnt());
		}*/
		
		StoreChannelInfo filterMap = null;
		filterMap = dbList.stream().filter(x->CommonUtils.in(list,x.getId()) && !CommonUtils.in(blackList,x.getId())
				).sorted(Comparator.comparing(StoreChannelInfo::getCnt)).findFirst().orElse(null);
		
		System.out.println(filterMap.getId()+","+filterMap.getCnt());
		List lista = null;
		do{
			System.out.println("liox");
			lista = new ArrayList();
			//break;
		}while(lista != null);
       
		
	
		
	}
	
	

}

 class StoreChannelInfo{
	 private long id;
	 private long cnt;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCnt() {
		return cnt;
	}
	public void setCnt(long cnt) {
		this.cnt = cnt;
	}
	 
 }

class Lock1 implements Runnable{
    @Override
    public void run(){
        try{
            System.out.println("Lock1 running");
            while(true){
                synchronized(Test.obj1){
                    System.out.println("Lock1 lock obj1");
                    Thread.sleep(3000);//获取obj1后先等一会儿，让Lock2有足够的时间锁住obj2
                    synchronized(Test.obj2){
                        System.out.println("Lock1 lock obj2");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
class Lock2 implements Runnable{
    @Override
    public void run(){
        try{
            System.out.println("Lock2 running");
            while(true){
                synchronized(Test.obj2){
                    System.out.println("Lock2 lock obj2");
                    Thread.sleep(3000);
                    synchronized(Test.obj1){
                        System.out.println("Lock2 lock obj1");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

