// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples.persistence;

import com.google.inject.Inject;

import com.aol.one.patch.PatchOperation;
import com.aol.one.patch.PatchOperationList;
import com.aol.one.patch.Patcher;
import com.aol.one.patch.PatcherFactory;
import com.aol.one.patch.examples.AppException;
import com.aol.one.patch.examples.persistence.model.Cart;
import com.aol.one.patch.examples.persistence.model.CartWithProducts;
import com.aol.one.patch.examples.persistence.model.PatchableCart;
import com.aol.one.patch.examples.persistence.service.ServicesHolder;

import org.apache.ibatis.session.SqlSession;
import org.glassfish.jersey.examples.httppatch.PATCH;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/19/16.
 */

@Path("/carts")
public class CartsResource {

  private ServicesHolder servicesHolder;

  @Inject
  public CartsResource(ServicesHolder servicesHolder) {
    this.servicesHolder = servicesHolder;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Cart> getAllCarts() {
    try {
      return servicesHolder.getCartService().getAllCarts();
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Cart createCart(Cart cart) {

    if (cart == null) {
      throw new AppException("empty cart specified");
    }

    if (cart.getId() != 0) {
      throw new AppException("id cannot be specified here");
    }

    try {
      servicesHolder.getCartService().createCart(cart);
      return cart;
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{cartid}")
  public Cart getCart(@PathParam("cartid") int cartId, @QueryParam("view") String view) {
    try {
      if (view != null && view.equalsIgnoreCase("products")) {
        return  servicesHolder.getCartService().getCartWithProducts(cartId);
      } else {
        return servicesHolder.getCartService().getCartById(cartId);
      }
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{cartid}")
  public void updateCart(@PathParam("cartid") int cartId, Cart cart) {
    try {
      cart.setId(cartId);
      servicesHolder.getCartService().saveCart(cart);
    } catch (Exception ex) {
      throw new AppException(ex);
    }
  }



  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{cartId}")
  @Produces("text/plain")
  public String doPatch(@PathParam("cartId")
                        final int cartId,
                       /* accept a list of operations */
                        PatchOperationList patchOperationList) throws IOException {

    StringBuilder sb = new StringBuilder();
    Patcher patcher = PatcherFactory.getDefaultPatcher();

    Cart cart = servicesHolder.getCartService().getCartById(cartId);

    if (cart == null) {
      throw new AppException("unknown cart id: " + cartId);
    }


    SqlSession sqlSession = null;
    try {

      sqlSession = servicesHolder.getCartService().getSqlSession();

      CartWithProducts cartWithProducts = servicesHolder.getCartService().getCartWithProducts(sqlSession, cartId);
      sb.append("Cart before ALL patch operations: ");
      sb.append(cartWithProducts);
      sb.append("\n-------------------\n");


      PatchableCart patchableCart = new PatchableCart(sqlSession, servicesHolder, cartId);

      for (PatchOperation patchOperation : patchOperationList) {
        patcher.patch(patchableCart, patchOperation);
        sb.append("\nCart after patch operation [" + patchOperation + "] : ");
        cartWithProducts = servicesHolder.getCartService().getCartWithProducts(sqlSession, cartId);
        sb.append(cartWithProducts);
        sb.append("\n-------------------\n");
      }

      // commit at end
      sqlSession.commit();

      cartWithProducts = servicesHolder.getCartService().getCartWithProducts(sqlSession, cartId);
      sb.append("Cart after ALL patch operations: ");
      sb.append(cartWithProducts);
      sb.append("\n-------------------\n");

    } catch (Exception ex) {
      sb.append("Exception occurred during patch: " + ex.toString());
    } finally {
      if (sqlSession != null) {
        sqlSession.close();
      }
    }

    sb.append("\n");
    return sb.toString();
  }

}
