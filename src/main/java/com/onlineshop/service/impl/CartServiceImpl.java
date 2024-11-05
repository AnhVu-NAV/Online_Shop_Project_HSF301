import com.onlineshop.dao.ProductDAO;
import com.onlineshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onlineshop.dao.CartDAO;
import com.onlineshop.entity.Cart;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;

@Service
public class CartServiceImpl implements CartService {

//    @Autowired
//    private CartDAO cartDAO;
//
//    @Autowired
//    private ProductDAO productDAO;
//
//    @Override
//    public void addProductToCart(Long productId, int quantity) {
//        Product product = productDAO.findById(productId);
//        Cart cart = getCurrentCart();
//
//        CartItem item = cart.getItems().stream()
//                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
//                .findFirst()
//                .orElse(null);
//
//        if (item == null) {
//            item = new CartItem();
//            item.setProduct(product);
//            item.setQuantity(quantity);
//            cart.getItems().add(item);
//        } else {
//            item.setQuantity(item.getQuantity() + quantity);
//        }
//
//        cartDAO.save(cart);
//    }
//
//    @Override
//    public void removeProductFromCart(Long productId) {
//        Cart cart = getCurrentCart();
//        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
//        cartDAO.save(cart);
//    }
//
//    @Override
//    public void updateProductQuantity(Long productId, int quantity) {
//        Cart cart = getCurrentCart();
//        cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst()
//                .ifPresent(item -> item.setQuantity(quantity));
//        cartDAO.save(cart);
//    }
//
//    @Override
//    public Cart getCurrentCart() {
//        return cartDAO.getCurrentCart(); // Lấy giỏ hàng hiện tại từ DAO
//    }
}
