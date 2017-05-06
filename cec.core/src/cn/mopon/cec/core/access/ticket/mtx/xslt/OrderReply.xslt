<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetOrderStateResult">
		<GetOrderStateResult>
			<ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<Order>
				<voucher>
					<printCode><xsl:value-of select="OrderNo" /></printCode>
					<verifyCode><xsl:value-of select="ValidCode" /></verifyCode>
				</voucher>
				<cinemaOrderCode><xsl:value-of select="OrderNo" /></cinemaOrderCode>
				<status>
					<xsl:choose>
						<xsl:when test="OrderStatus='7'">6</xsl:when>
						<xsl:when test="OrderStatus='8'">4</xsl:when>
						<xsl:when test="OrderStatus='9'">4</xsl:when>
						<xsl:otherwise>3</xsl:otherwise>
					</xsl:choose>
				</status>
				<orderItems>
					<orderItem>
						<ticketCode>
							<xsl:value-of select="OrderNo" />
						</ticketCode>
					</orderItem>
				</orderItems>
			</Order>
		</GetOrderStateResult>
	</xsl:template>
</xsl:stylesheet>