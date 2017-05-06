<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data">
		<data>
			<result>
				<xsl:value-of select="bookingCancel/@result"></xsl:value-of>
			</result>
			<message><xsl:value-of select="bookingCancel/@message"></xsl:value-of></message>
			<Order>
				<voucher>
					<verifyCode>
						<xsl:value-of select="bookingCancel/confirmationId" />
					</verifyCode>
					<printCode>
						<xsl:value-of select="bookingCancel/bookingId" />
					</printCode>
				</voucher>
				<cinemaOrderCode>
					<xsl:value-of select="bookingCancel/bookingId" />
				</cinemaOrderCode>
				<orderItems>
					<xsl:for-each select="bookingCancel/seats/seat">
						<orderItem>
							<ticketCode>
								<xsl:value-of select="@ticketNo" />
							</ticketCode>
							<seatCode>
								<xsl:value-of select="@rowId" />
								_
								<xsl:value-of select="@columnId" />
							</seatCode>
						</orderItem>
					</xsl:for-each>
				</orderItems>
			</Order>
		</data>
	</xsl:template>
</xsl:stylesheet>