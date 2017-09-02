import java.io.*;
import java.sql.*;
import java.util.*;
import java.nio.file.*;
import java.lang.Thread;
public class Spyfall{
    public static void main(String[] args){
		Path file= FileSystems.getDefault().getPath("Locations.txt");
        List<Location> list = new ArrayList();
		try(InputStream in=Files.newInputStream(file);
		BufferedReader reader=new BufferedReader(new InputStreamReader(in))){
			String line=null;
			while((line=reader.readLine())!=null){
				String[] temp=line.split(", ");
				String name=temp[0];
				String[] r= new String[10];
				for(int i=1;i<temp.length;i++){
					if(temp[i].contains("\n")){
						r[i-1]=temp[i].replace("\n","");
					}
					else{
						r[i-1]=temp[i];
					}
				}
				if(r[9]!=null){
					list.add(new Location(name,r));
				}
			}
		}catch (IOException x){
			System.err.println(x);
		}
		Random r=new Random();
		Location set=list.get(r.nextInt(list.size()-1));
		String[] game=set.setRoles(10);
		System.out.println(set.getLoc());
		for(String s:game){
			if(s.equals("Spy")){
				System.out.print("\tTRUE ");
			}
			System.out.println(s);
		}
    }
}
class Location{
    private String name; private String[] roles;
    public Location(String name, String[] roles){
        this.name=name; this.roles=roles;
    }
    public String getLoc(){
		return name;
	}
    public String[] getRoles(){
		return roles;
	}
    public String[] setRoles(int n){
		String[] result = new String[n];
        Random r = new Random();
		List<String> l= new ArrayList(Arrays.asList(roles));
		setRoles(n-1,0,result,r,l);
        result[r.nextInt(n-1)]="Spy";
		return result;
	}
    private void setRoles(int n,int i,String[] result,Random r, List<String> l){
		if(l.size()==1){
			result[i]=l.get(0);
		}
		else{
			result[i]=l.get(r.nextInt(l.size()-1));
			l.remove(result[i]);
			if(i<n){
				setRoles(n,++i,result,r,l);
			}
		}
    }
}