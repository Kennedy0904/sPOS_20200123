package com.example.dell.smartpos;

public class Mod10 {
	public static boolean mod10(String cardno)
	{ 
		try {
			int sum = 0;
			for(int i=cardno.length()-1; i>=0; i=i-2)
			{
				sum = sum + Integer.parseInt(String.valueOf(cardno.charAt(i)));
				
				if(i-1 >= 0)
				{
					int product = Integer.parseInt(String.valueOf(cardno.charAt(i-1))) * 2;
				
					if(product > 8)
						sum = sum + 1 + (product - 10);
					else
						sum = sum + product;
				}
			}
			if(((sum / 10) * 10) - sum < 0)
				return false;			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
