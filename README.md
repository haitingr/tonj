波场链对接JAVA

修复了原有的BUG
升级支持JDK21，更新所有的依赖包

实例
#TEST

    network.rpc.url=https://nile.trongrid.io/wallet/
    network.grpc.endpoint=grpc.nile.trongrid.io:50051
    network.grpc.endpoint.solidity=grpc.nile.trongrid.io:50061
    network.grpc.apikey=xxxx

#PRODUCT

    network.rpc.url=https://api.trongrid.io/wallet/
    network.grpc.endpoint=grpc.trongrid.io:50051
    network.grpc.endpoint.solidity=grpc.trongrid.io:50052
    network.grpc.apikey=xxxx


创建API对接


发送TRX

    ApiWrapper wrapper = TronNetwork.getInstance().getGrpcApi(wallet.key().getPrivateKeyAsHex());
		try {
			TransactionExtention tex = wrapper.transfer(fromAddress, toAddress, value);
			logger.info("transaction:{}", tex);
			logger.info("transaction.Txid:{}", tex.getTxid());
			// 签名交易
			Chain.Transaction signTransaction = wrapper.signTransaction(tex);
			logger.info("signTransaction:{}", signTransaction);
			// 计算交易所需要的宽带
			long byteSize = wrapper.estimateBandwidth(signTransaction);
			logger.info("byteSize:{}", byteSize);
			// 广播交易
			String hashTx = wrapper.broadcastTransaction(signTransaction);
			logger.info("hashTRX:{}", hashTx);
			return hashTx;
		} catch (IllegalException e) {
			throw new IoTokenException(e);
		} finally {
      //长连接可以不关闭
			wrapper.close();
		}

获取TRX余额

    ApiWrapper wrapper = TronNetwork.getInstance().getGrpcApi(wallet.key().getPrivateKeyAsHex());
		try {

			long value = wrapper.getAccountBalance(address);
			return BigDecimal.valueOf(value, this.decimal());
		} catch (Exception e) {
			throw new IoTokenException(e);
		} finally {
			wrapper.close();
		}

发送TRC20

    ApiWrapper wrapper = TronNetwork.getInstance().getGrpcApi(wallet.key().getPrivateKeyAsHex());
		try {
		
			//wrapper.getTransactionInfoById(toAddress).getFee()tf
			// 获取合约地址信息
			Contract contract = wrapper.getContract(this.address);
			Trc20Contract token = new Trc20Contract(contract, fromAddress, wrapper);
			String txid = "";
			// 获取转账账户的TRX余额
			BigInteger trc20Value = token.balanceOf(fromAddress);
			BigDecimal trc20V=new BigDecimal(trc20Value,this.decimal);
			// 获取想要转账的数额
			int power=0;
			int newdecimal=decimal;
      //内部的BUG，对于精度太高的TOKEN会出现问题，通过这个方式修复
			if(decimal>8) {
				newdecimal=8;
				power=decimal-8;
			}
			
			BigInteger sunAmountValue = amount.multiply(BigDecimal.valueOf(10).pow(newdecimal)).toBigInteger();

			logger.info("Transfer Token TRC20 {} , Amount {}", this.name, amount.toPlainString());
			
			logger.info("Account Balance :{}",trc20V.toPlainString());
			// 进行比较
			if (trc20V.compareTo(amount) >= 0) {
				logger.info("Begin Transfer .........");
				// 设置最大矿工费用
				long feeLimit = 100000000;
				// 转账
				txid = token.transfer(toAddress, sunAmountValue.longValue(), power, "Transfer", feeLimit);
				
				logger.info("Transfer TX ID {}",txid);
			} else {
				logger.error("Not Enogpht");
			}
			
			return txid;

获取TRC20 余额

    ApiWrapper wrapper = TronNetwork.getInstance().getGrpcApi(wallet.key().getPrivateKeyAsHex());
    try {
      Contract contract = wrapper.getContract(this.address);
      Trc20Contract t = new Trc20Contract(contract, address, wrapper);
      BigInteger value = t.balanceOf(address);
      //System.out.println(value);
      return new BigDecimal(value, this.decimal);
    } catch (Exception e) {
      throw new IoTokenException(e);
    } finally {
      wrapper.close();
    }

获取API为伪代码，请自行封装，有用请表星
Telegram @osnest

