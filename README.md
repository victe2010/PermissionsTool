android6.0权限管理
========
## Android 6.0版本(Api 23)推出了很多新的特性, 大幅提升了用户体验, 同时也为程序员带来新的负担. 动态权限管理就是这样, 
* 一方面让用户更加容易的控制自己的隐私, 
* 一方面需要重新适配应用权限. 
##### 时代总是不断发展, 程序总是以人为本, 让我们为应用添加动态权限管理吧! 
##### 这里提供了一个非常不错的解决方案, 提供源码, 项目可以直接使用.

## 注意
android原生的系统拒绝授权之后，可以再次请求申请权限，但一些手机系统定制了权限管理，拒绝一次后不再申请权限，需要在
设置功能中手动设置权限

引用指南
-------------------------
## 第一步 在存储库的末尾你根 build.gradle 中添加︰

<pre>
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
</pre>

## 第 2 步。添加依赖项
<pre>
  	dependencies {
	     compile 'com.github.victe2010:PermissionsTool:1.0'
	}

</pre>


## 使用方式
-------------------------
### 在activity中使用
<pre>
  	PermissionTool.getInstance()
                .with(this)
                .callBack(callBack)//回调
                .requestCode(0x123)//请求码
                .requestpermissions(Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE)//请求权限
                .start();	
</pre>

### 回调事件
<pre>
 private PermissionTool.CallBack callBack = new PermissionTool.CallBack() {
        @Override
        public void onSuccess(int requestCode, List<String> grantPermissions) {
            //权限申请成功  requestCode 请求码   grantPermissions已授权的权限名称

        }

        @Override
        public void onFail(int requestCode, List<String> deniedPermissions) {
            //权限申请失败  requestCode 请求码   deniedPermissions拒绝授权的权限名称
        }
    };
</pre>




















