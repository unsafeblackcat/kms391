import java.io.File;

public class FindQuestAPI {

	// 定义 Quest.wz 文件夹中 QuestData 文件夹的路径
	private static String path = "E:\\Development\\Source\\Java\\391\\wz\\Quest.wz\\QuestData";

	public static void main(String[] args) {
		// 创建一个 File 对象，指向指定的路径
		File dir = new File(path);

		// 检查路径是否存在且是否为一个目录
		if (!dir.exists() || !dir.isDirectory()) {
			System.err.println("指定的路径不存在或不是一个有效的目录: " + path);
			return;
		}

		// 获取目录下的所有文件
		File[] files = dir.listFiles();

		// 检查是否成功获取到文件列表
		if (files == null) {
			System.err.println("无法获取指定目录下的文件列表: " + path);
			return;
		}

		// 获取文件数组的长度
		int size = files.length;

		// 用于存储最终结果的字符串
		StringBuilder result = new StringBuilder();

		// 遍历文件数组
		for (int i = 0; i < size; i++) {
			// 检查文件是否为文件（不是目录）
			if (files[i].isFile()) {
				// 获取文件名
				String fileName = files[i].getName();
				// 检查文件名是否包含 ".img" 后缀
				if (fileName.endsWith(".img")) {
					// 分割文件名，去除 ".img" 后缀
					String questId = fileName.split("\\.img")[0];
					// 将分割后的文件名添加到结果字符串中
					result.append(questId);
					// 如果不是最后一个文件，添加逗号和空格
					if (i != (size - 1)) {
						result.append(", ");
					}
				}
			}
		}

		// 输出最终结果
		System.out.println(result.toString());
	}
}