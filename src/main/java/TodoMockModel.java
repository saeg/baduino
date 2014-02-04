import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;

public class TodoMockModel  {
	

  public List<Category> getCategories() {
	  
	IMethod[] methodList = DataFlowMethodView.methods;
	List<Category> categories = new ArrayList<Category>();
	Category category = null;

	for(IMethod method:methodList){
		category = new Category();
		category.setName(formatName(method.toString()));
		categories.add(category);
	}
  
   	category = new Category();
    category.setName("Programming");
    categories.add(category);
    Todo todo = new Todo("Write more about e4");
    category.getTodos().add(todo);
    todo = new Todo("Android", "Write a widget.");
    category.getTodos().add(todo);
    
    category = new Category();
    category.setName("Leasure");
    categories.add(category);
    todo = new Todo("Skiing");
    category.getTodos().add(todo);
    
    category = new Category();
    category.setName("method_1");
    categories.add(category);
    return categories;
  }
  
  private String formatName(String method){
	 
	return method;
	  
  }

} 