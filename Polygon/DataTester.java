import java.io.*;

public class DataTester {
	public static void main(String[] args) throws IOException {
		FileInputStream in = new FileInputStream("poly.dat");
		DataInputStream din = new DataInputStream(in);
		
		System.out.println(din.readInt());
		int vertCount = din.readInt();
		System.out.println(vertCount);
		System.out.println(din.readBoolean());
		for(int i = 0; i < 2 * vertCount; i++) {
			System.out.println(din.readInt());
		}
		System.out.println("R");
		System.out.println(din.readInt());
		System.out.println("G");
		System.out.println(din.readInt());
		System.out.println("B");
		System.out.println(din.readInt());
		
		din.close();
	}
}
