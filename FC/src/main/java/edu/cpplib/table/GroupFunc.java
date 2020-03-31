package edu.cpplib.table;

import java.util.ArrayList;

import edu.cpplib.StrFunction.StrFunc;

public class GroupFunc {

	public static ArrayList<TItem> assignRealPgNum(ArrayList<TItem> allTableItems1){
		
		ArrayList<TItem> allTableItems = allTableItems1;
		
		for(TItem ti : allTableItems){
//			ti.print();
			if(StrFunc.isAlpha(ti.getTargetPgNum())){
				if(StrFunc.romanIsParsed(ti.getTargetPgNum())) continue;
				if(ti.getTargetPgNum().equals("NEXT")) continue;
				ti.setTargetPgNum("2000");
			}
		}
		
		
		
		int counter = 0;
		for(TItem ti : allTableItems){
			
			try{
				if(StrFunc.INTIsParsed(ti.getTargetPgNum()) || StrFunc.romanIsParsed(ti.getTargetPgNum())){
					counter ++;
				}
			}
			catch(Exception e){
			}
		}
		
//		System.out.println(counter + "?????");
		
		if(counter == allTableItems.size()){
//			System.out.println("counter == allTableItems.size()");
			return allTableItems;
		}
		
		else{
//			System.out.println("counter != allTableItems.size()");
			for(int i=0; i<allTableItems.size(); ++i){
				if(allTableItems.get(i).getTargetPgNum().equals("NEXT")){
					try{
						int n = Integer.parseInt(allTableItems.get(i+1).getTargetPgNum());
						allTableItems.get(i).setTargetPgNum(allTableItems.get(i+1).getTargetPgNum());
						allTableItems.get(i).setRealPgNum(allTableItems.get(i+1).getRealPgNum());
						
					}
					catch(Exception e){
						allTableItems.get(i).setTargetPgNum(allTableItems.get(i+1).getTargetPgNum());
					}
				}
			}
//			
			assignRealPgNum(allTableItems);
			return allTableItems;
			/*
			 *         for i in range(1, len(list1)):
			            if(list1[i] == "NEXT"):
			                try:
			                    num = int(list1[i+1])
			                    list1[i] = list1[i+1]
			                except:
			                    list1[i] = list1[i+1]
			        group(list1)
			        return list1
			 */
		}
		
	}
	/*
	 * def group(list1):
    
    counter = 0
    for l in list1:
        try:
            int(l)
            counter += 1
        except:
            counter = -1
    
    if(counter == len(list1)):
        return list1
        
    else:
        for i in range(1, len(list1)):
            if(list1[i] == "NEXT"):
                try:
                    num = int(list1[i+1])
                    list1[i] = list1[i+1]
                except:
                    list1[i] = list1[i+1]
        group(list1)
        return list1
	 */
	
	
	

	
	
	/*
	 * 	Item: Table2:MinimumandmaximumRMSvaluesatdifferentscanlengthsandetchtimes, target page num: NEXT( false ), real page num: -1
		Item: forLE4,andtheircorrespondingetchrates.Otherroughnessvaluesareavailableinthe, target page num: NEXT( false ), real page num: -1
		Item: appendix.*Roughnessvaluesfrom1?mscanlength, target page num: 57( false ), real page num: 67
		
	 *  Should be able to group these 3 items together
	 *  Or at least assign correct page number to item with NEXT
	 */
	
	
}
